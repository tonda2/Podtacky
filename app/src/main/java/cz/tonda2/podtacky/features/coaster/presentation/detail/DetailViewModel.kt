package cz.tonda2.podtacky.features.coaster.presentation.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.presentation.ScreenState
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.DetailScreen.ID] ?: 1L

    private val _screenStateStream = MutableStateFlow(DetailScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            _screenStateStream.update {
                val coaster = coasterRepository.getCoasterById(id.toString())
                it.copy(
                    coaster = coaster,
                    folderName = if (coaster?.folderUid == null) ""
                    else folderRepository.getFolderByUid(
                        coaster.folderUid.toString()
                    )?.name ?: ""
                )
            }
        }
    }

    fun delete(context: Context) {
        _screenStateStream.update {
            it.copy(
                state = ScreenState.Loading
            )
        }
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val frontUri = _screenStateStream.value.coaster?.frontUri
            val backUri = _screenStateStream.value.coaster?.backUri

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

            val coaster = _screenStateStream.value.coaster
            if (coaster != null) {
                if (coaster.uploaded) {
                    coasterRepository.markDeleted(coaster.coasterId.toString())
                }
                else {
                    coasterRepository.deleteCoaster(coaster)
                }
            }
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
    val folderName: String = "",
    val state: ScreenState = ScreenState.Fill
)