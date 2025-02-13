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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class FolderListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val uid: String
        get() = savedStateHandle[Screen.FolderScreen.UID] ?: "-"

    private val _folderListUiState = MutableStateFlow(FolderListScreenState())
    val folderListUiState: StateFlow<FolderListScreenState> = _folderListUiState

    init {
        viewModelScope.launch {
            combine(
                if (uid == "-") folderRepository.getFoldersWithoutParent() else folderRepository.getSubFolders(uid),
                if (uid == "-") coasterRepository.getCoastersWithoutFolder() else coasterRepository.getCoastersInFolder(uid)
            ) { subfolders, coasters ->
                FolderListScreenState(
                    parentFolder = folderRepository.getFolderByUid(uid),
                    subFolders = subfolders.filter { f -> !f.deleted }.sortedBy { it.name.lowercase() },
                    coasters = coasters.filter { c -> !c.deleted }.sortedBy { it.brewery.lowercase() }
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
            folderUid = UUID.randomUUID().toString(),
            name = _folderListUiState.value.newFolderName.trim(),
            parentUid = if (uid != "-") uid else null,
            uploaded = false,
            deleted = false
        )

        viewModelScope.launch {
            folderRepository.addFolder(newFolder)
        }
    }

    fun deleteFolder() {
        viewModelScope.launch {
            val folder = _folderListUiState.value.folderToDelete ?: return@launch

            val subFolders = folderRepository.getSubFolders(folder.folderUid).first()
            subFolders.forEach { subFolder ->
                val newFolder = subFolder.copy(parentUid = folder.parentUid, uploaded = false)
                folderRepository.updateFolder(newFolder)
            }

            val coastersInFolder = coasterRepository.getCoastersInFolder(folder.folderUid).first()
            coastersInFolder.forEach { coaster ->
                coasterRepository.addCoaster(coaster.copy(coasterId = 0L, folderUid = folder.parentUid, uploaded = false))

                if (coaster.uploaded) {
                    coasterRepository.markDeleted(coaster.coasterId.toString())
                } else {
                    coasterRepository.deleteCoaster(coaster)
                }
            }

            // Delete at the end to keep FK working
            if (folder.uploaded) {
                folderRepository.markDeleted(folder.folderUid)
            }
            else {
                folderRepository.deleteFolder(folder)
            }
        }
    }

    fun startRename(folder: Folder) {
        _folderListUiState.value = _folderListUiState.value.copy(folderBeingRenamed = folder, changedName = folder.name)
    }

    fun setToDelete(folder: Folder) {
        _folderListUiState.value = _folderListUiState.value.copy(folderToDelete = folder)
    }

    fun updateRename(name: String) {
        _folderListUiState.value = _folderListUiState.value.copy(changedName = name)
    }

    fun renameFolder() {
        viewModelScope.launch {
            val folder = _folderListUiState.value.folderBeingRenamed ?: return@launch
            val newFolder = folder.copy(name = _folderListUiState.value.changedName, uploaded = false)
            folderRepository.updateFolder(newFolder)
        }
    }
}

data class FolderListScreenState(
    val parentFolder: Folder? = null,
    val newFolderName: String = "",
    val subFolders: List<Folder> = emptyList(),
    val coasters: List<Coaster> = emptyList(),
    val folderBeingRenamed: Folder? = null,
    val changedName: String = "",
    val folderToDelete: Folder? = null
)