package cz.tonda2.podtacky.features.coaster.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ListViewModel(
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val _screenStateLiveData = MutableLiveData<ListScreenState>()
    val screenStateLiveData: LiveData<ListScreenState> = _screenStateLiveData

    init {
        viewModelScope.launch {
            coasterRepository.getCoastersLive().observeForever { coasters ->
                _screenStateLiveData.value = ListScreenState(
                    coasters = sortCoastersByType(coasters?.filter { !it.deleted } ?: emptyList(), CoasterSortType.DATE)
                )
            }
        }
    }

    private fun sortCoastersByType(coasters: List<Coaster>, order: CoasterSortType): List<Coaster> {
        val formatter = SimpleDateFormat("dd.MM.yyy")

        return when (order) {
            CoasterSortType.DATE -> coasters.sortedByDescending { formatter.parse(it.dateAdded) }
            CoasterSortType.BREWERY -> coasters.sortedBy { it.brewery.lowercase() }
            CoasterSortType.COUNT -> coasters.sortedByDescending { it.count }
        }
    }

    fun updateSortOrder(newOrder: CoasterSortType) {
        val currentState = _screenStateLiveData.value ?: return

        _screenStateLiveData.value = currentState.copy(
            coasters = sortCoastersByType(currentState.coasters, newOrder),
            order = newOrder
        )
    }

    fun getSelectedIndex(): Int {
        return CoasterSortType.entries.indexOf(_screenStateLiveData.value?.order)
    }
}

data class ListScreenState(
    val coasters: List<Coaster> = emptyList(),
    val order: CoasterSortType = CoasterSortType.DATE
)