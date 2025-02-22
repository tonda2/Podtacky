package cz.tonda2.podtacky.features.coaster.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.data.PreferencesManager
import cz.tonda2.podtacky.core.presentation.sortCoastersByType
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModel(
    private val coasterRepository: CoasterRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _order = MutableStateFlow(preferencesManager.getSortOrder())

    val listUiState: StateFlow<ListScreenState> = _order
        .flatMapLatest { order ->
            coasterRepository.getUndeletedCoastersList()
                .map {
                    ListScreenState(
                        coasters = sortCoastersByType(it, order),
                        order = order
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ListScreenState()
        )

    fun updateSortOrder(newOrder: CoasterSortType): Boolean {
        val currentOrder = _order.value
        if (currentOrder == newOrder) return false

        _order.value = newOrder
        preferencesManager.saveSortOrder(newOrder)
        return true
    }

    fun getSelectedIndex(): Int {
        return CoasterSortType.entries.indexOf(_order.value)
    }
}

data class ListScreenState(
    val coasters: List<Coaster> = emptyList(),
    val order: CoasterSortType = CoasterSortType.BREWERY
)