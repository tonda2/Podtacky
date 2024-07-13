package cz.tonda2.podtacky.features.coaster.presentation.large_photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LargePhotoViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.LargePhotoScreen.ID] ?: 1L

    private val _screenStateStream = MutableStateFlow(PhotoScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            _screenStateStream.update {
                it.copy(coaster = coasterRepository.getCoasterById(id.toString()))
            }
        }
    }
}

data class PhotoScreenState (
    val coaster: Coaster? = null,
)