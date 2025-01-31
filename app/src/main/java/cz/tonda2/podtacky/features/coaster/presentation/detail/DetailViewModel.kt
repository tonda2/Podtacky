package cz.tonda2.podtacky.features.coaster.presentation.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val id: Long
        get() = savedStateHandle[Screen.DetailScreen.ID] ?: 1L

    val detailUiState: StateFlow<DetailScreenState> = coasterRepository
        .getCoasterById(id.toString())
        .map { DetailScreenState(coaster = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DetailScreenState()
        )

    fun delete(context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val frontUri = detailUiState.value.coaster?.frontUri
            val backUri = detailUiState.value.coaster?.backUri

            try {
                var delCount = 0
                if (frontUri != null && frontUri.toString().isNotEmpty()) {
                    delCount += contentResolver.delete(frontUri, null, null)
                }
                if (backUri != null && backUri.toString().isNotEmpty()) {
                    delCount += contentResolver.delete(backUri, null, null)
                }
                Log.d("Deleting", "Deleted $delCount images")
            } catch (e: Exception) {
                Log.e("Deleting", e.toString())
            }

            val coaster = detailUiState.value.coaster
            if (coaster != null) {
                if (coaster.uploaded) {
                    coasterRepository.markDeleted(coaster.coasterId.toString())
                }
                else {
                    coasterRepository.deleteCoaster(coaster)
                }
            }
        }
    }
}

data class DetailScreenState(
    val coaster: Coaster? = null
)