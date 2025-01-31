package cz.tonda2.podtacky.features.coaster.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.text.SimpleDateFormat

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModel(
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _order = MutableStateFlow(CoasterSortType.DATE)

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

    private fun sortCoastersByType(coasters: List<Coaster>, order: CoasterSortType): List<Coaster> {
        val formatter = SimpleDateFormat("dd.MM.yyy")

        return when (order) {
            CoasterSortType.DATE -> coasters.sortedByDescending { formatter.parse(it.dateAdded) }
            CoasterSortType.BREWERY -> coasters.sortedBy { it.brewery.lowercase() }
            CoasterSortType.COUNT -> coasters.sortedByDescending { it.count }
        }
    }

    fun updateSortOrder(newOrder: CoasterSortType): Boolean {
        val currentOrder = _order.value
        if (currentOrder == newOrder) return false

        _order.value = newOrder
        return true
    }

    fun getSelectedIndex(): Int {
        return CoasterSortType.entries.indexOf(_order.value)
    }
}

data class ListScreenState(
    val coasters: List<Coaster> = emptyList(),
    val order: CoasterSortType = CoasterSortType.DATE
)