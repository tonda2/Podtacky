package cz.tonda2.podtacky.features.coaster.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.launch

class ListViewModel(
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val _screenStateLiveData = MutableLiveData<ListScreenState>()
    val screenStateLiveData: LiveData<ListScreenState> = _screenStateLiveData

    init {
        viewModelScope.launch {
            coasterRepository.getCoastersLive().observeForever { coasters ->
                _screenStateLiveData.value = ListScreenState(coasters?.filter { !it.deleted } ?: emptyList())
            }
        }
    }
}

data class ListScreenState(
    val coasters: List<Coaster> = emptyList()
)