package cz.cvut.fit.podtacky.features.coaster.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.core.presentation.Screen
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import cz.cvut.fit.podtacky.features.coaster.presentation.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.DetailScreen.ID] ?: 1L

    private val _screenStateStream = MutableStateFlow(DetailScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            _screenStateStream.update {
                it.copy(coaster = coasterRepository.getCoasterById(id.toString()))
            }
        }
    }

    fun delete() {
        _screenStateStream.update {
            it.copy(
                state = ScreenState.Loading
            )
        }
        viewModelScope.launch {
            coasterRepository.deleteCoaster(
                _screenStateStream.value.coaster!!
            )
        }
        _screenStateStream.update {
            it.copy(
                state = ScreenState.Fill
            )
        }
    }
}

data class DetailScreenState(
    val coaster: Coaster? = null,
    val state: ScreenState = ScreenState.Fill
)