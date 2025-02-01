package cz.tonda2.podtacky.features.coaster.presentation.detail

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.launch

class DetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.DetailScreen.ID] ?: 1L

    var detailUiState by mutableStateOf(DetailScreenState())
        private set

    init {
        viewModelScope.launch {
            val coaster = coasterRepository.getCoasterById(id.toString())

            detailUiState = DetailScreenState(
                coaster = coaster
            )
        }
    }

    fun delete(context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val frontUri = detailUiState.coaster?.frontUri
            val backUri = detailUiState.coaster?.backUri

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

            val coaster = detailUiState.coaster
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