package cz.tonda2.podtacky.features.coaster.presentation.list

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.core.data.PreferencesManager
import cz.tonda2.podtacky.core.presentation.sortCoastersByType
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModel(
    private val coasterRepository: CoasterRepository,
    private val preferencesManager: PreferencesManager,
    private val resources: Resources
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _listUiState: MutableStateFlow<ListScreenState> = MutableStateFlow(ListScreenState())
    val listUiState: StateFlow<ListScreenState> = _listUiState
    private val _order = MutableStateFlow(preferencesManager.getSortOrder())
    private val _titleType = MutableStateFlow(ListScreenTitleType.UNIQUE)

    val titleText: StateFlow<String> = combine(
        listUiState,
        _titleType
    ) { state, titleType ->
        when (titleType) {
            ListScreenTitleType.UNIQUE -> {
                val count = state.coasters.size
                when (count) {
                    1 -> resources.getString(R.string._1_podtacek)
                    in 2..4 -> resources.getString(R.string._2_4_podtacky, count)
                    else -> resources.getString(R.string._5_podtacku, count)
                }
            }
            ListScreenTitleType.TOTAL -> {
                val totalCount = state.coasters.sumOf { it.count }
                when (totalCount) {
                    1 -> resources.getString(R.string._1_kus)
                    in 2..4 -> resources.getString(R.string._2_4_kusy, totalCount)
                    else -> resources.getString(R.string._5_kusu, totalCount)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = resources.getString(R.string._5_podtacku, 0)
    )

    init {
        viewModelScope.launch {
            combine(
                coasterRepository.getUndeletedCoastersList(),
                _order,
                _titleType
            ) { coasters, order, titleType ->
                ListScreenState(
                    coasters = sortCoastersByType(coasters, order),
                    order = order,
                    titleType = titleType
                )
            }.collect { newState ->
                _listUiState.value = newState
            }
        }
    }

    fun updateSortOrder(newOrder: CoasterSortType): Boolean {
        val currentOrder = _order.value
        if (currentOrder == newOrder) return false

        _order.value = newOrder
        preferencesManager.saveSortOrder(newOrder)
        return true
    }

    fun switchTitle() {
        _titleType.value = when (_titleType.value) {
            ListScreenTitleType.TOTAL -> ListScreenTitleType.UNIQUE
            ListScreenTitleType.UNIQUE -> ListScreenTitleType.TOTAL
        }
    }

    fun getSelectedIndex(): Int {
        return CoasterSortType.entries.indexOf(_order.value)
    }
}

data class ListScreenState(
    val coasters: List<Coaster> = emptyList(),
    val order: CoasterSortType = CoasterSortType.BREWERY,
    val titleType: ListScreenTitleType = ListScreenTitleType.UNIQUE
)

enum class ListScreenTitleType {
    TOTAL,
    UNIQUE
}