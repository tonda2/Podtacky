package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FolderListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(FolderListScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    private val id: Long
        get() = savedStateHandle[Screen.FolderScreen.ID] ?: -1L

    init {
        viewModelScope.launch {
            /**
             * TODO
             * Pokud id není -1, jsem už ve složce -> nadpis jméno parenta, dát šipku zpět
             * Přes dao najít všechny složky s tímhle id jako parentem (pokud -1, tak null)
             * Pak použít FolderWIthCoasters pro najití podtácků v téhle složce -> jak pro ty bez šložky? Možná udělat hlavní složku 'vše', aby každý měl?
             */
//            if (id != -1L) {
//                val coaster = coasterRepository.getCoasterById(id.toString())
//                _screenStateStream.update {
//                    it.copy(
//                        title = "Upravit podtácek",
//                        oldCoaster = coaster,
//                        brewery = coaster.brewery,
//                        description = coaster.description,
//                        date = coaster.dateAdded,
//                        city = coaster.city,
//                        count = coaster.count.toString(),
//                        frontUri = coaster.frontUri,
//                        backUri = coaster.backUri
//                    )
//                }
//            }
        }
    }

    fun updateNewFolderName(name: String) {
        _screenStateStream.update {
            it.copy(
                newFolderName = name
            )
        }
    }

    fun addFolder() {
        val newFolder = Folder(
            name = _screenStateStream.value.newFolderName,
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