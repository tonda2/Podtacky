package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class FolderListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.FolderScreen.ID] ?: -1L

    private val _folderListUiState = MutableStateFlow(FolderListScreenState())
    val folderListUiState: StateFlow<FolderListScreenState> = _folderListUiState

    init {
        viewModelScope.launch {
            combine(
                if (id == -1L) folderRepository.getFoldersWithoutParent() else folderRepository.getSubFolders(id.toString()),
                if (id == -1L) coasterRepository.getCoastersWithoutFolder() else coasterRepository.getCoastersInFolder(id.toString())
            ) { subfolders, coasters ->
                FolderListScreenState(
                    parentFolder = folderRepository.getFolderById(id.toString()),
                    subFolders = subfolders,
                    coasters = coasters
                )
            }.collect { newState ->
                _folderListUiState.value = newState
            }
        }
    }

    fun updateNewFolderName(name: String) {
        _folderListUiState.value = _folderListUiState.value.copy(newFolderName = name)
    }

    fun addFolder() {
        val newFolder = Folder(
            uid = UUID.randomUUID().toString(),
            name = _folderListUiState.value.newFolderName.trim(),
            parentId = if (id != -1L) id else null,
            uploaded = false,
            deleted = false
        )

        viewModelScope.launch {
            folderRepository.addFolder(newFolder)
        }
    }
}

data class FolderListScreenState(
    val parentFolder: Folder? = null,
    val newFolderName: String = "",
    val subFolders: List<Folder> = emptyList(),
    val coasters: List<Coaster> = emptyList()
)