package cz.cvut.fit.podtacky.features.coaster.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(ListScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            // coasterRepository.addCoaster()
            _screenStateStream.value = ListScreenState(
                coasters = coasterRepository.getCoasters()
            )
        }
    }
}

data class ListScreenState(
    val coasters: List<Coaster> = emptyList()
)