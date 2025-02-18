package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.data.PreferencesManager
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.core.presentation.sortCoastersByType
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
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
    private val coasterRepository: CoasterRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val uid: String
        get() = savedStateHandle[Screen.FolderScreen.UID] ?: "-"

    private val _folderListUiState = MutableStateFlow(FolderListScreenState())
    val folderListUiState: StateFlow<FolderListScreenState> = _folderListUiState

    private val _order = MutableStateFlow(preferencesManager.getSortOrder())

    init {
        viewModelScope.launch {
            combine(
                if (uid == "-") folderRepository.getFoldersWithoutParent() else folderRepository.getSubFolders(uid),
                if (uid == "-") coasterRepository.getCoastersWithoutFolder() else coasterRepository.getCoastersInFolder(uid),
                _order
            ) { subfolders, coasters, order ->
                FolderListScreenState(
                    parentFolder = folderRepository.getFolderByUid(uid),
                    subFolders = subfolders.filter { f -> !f.deleted }.sortedBy { it.name.lowercase() },
                    coasters = sortCoastersByType(coasters.filter { c -> !c.deleted }, order)
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

            // Only mark as deleted in case folder was renamed and then deleted before being uploaded
            // After rename, uploaded is set to false - can't tell if it's false because it isn't backed up or because it was renamed
            folderRepository.markDeleted(folder.folderUid)
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
            if (folder.name == _folderListUiState.value.changedName.trim()) return@launch

            val newFolder = folder.copy(name = _folderListUiState.value.changedName.trim(), uploaded = false)
            folderRepository.updateFolder(newFolder)
        }
    }

    fun getSelectedIndex(): Int {
        return CoasterSortType.entries.indexOf(_order.value)
    }

    fun updateSortOrder(newOrder: CoasterSortType): Boolean {
        val currentOrder = _order.value
        if (currentOrder == newOrder) return false

        _order.value = newOrder
        preferencesManager.saveSortOrder(newOrder)
        return true
    }
}

data class FolderListScreenState(
    val parentFolder: Folder? = null,
    val newFolderName: String = "",
    val subFolders: List<Folder> = emptyList(),
    val coasters: List<Coaster> = emptyList(),
    val folderBeingRenamed: Folder? = null,
    val changedName: String = "",
    val folderToDelete: Folder? = null,
    val coasterOrder: CoasterSortType = CoasterSortType.BREWERY
)