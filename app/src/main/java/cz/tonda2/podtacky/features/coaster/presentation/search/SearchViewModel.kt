package cz.tonda2.podtacky.features.coaster.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.folder.data.FolderRepository
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val coasterRepository: CoasterRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(SearchScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    fun updateQuery(query: String) {
        viewModelScope.launch {
            _screenStateStream.update {
                it.copy(
                    query = query
                )
            }
        }
    }

    suspend fun searchCoasters() {
        _screenStateStream.update {
            it.copy(
                query = _screenStateStream.value.query.trim(),
                resultCoasters = coasterRepository.searchCoasters(_screenStateStream.value.query.trim())
                    .first()
                    .filter { coaster -> !coaster.deleted }
                    .sortedBy { coaster -> coaster.brewery.lowercase() },
                resultFolders = folderRepository.searchFolders(_screenStateStream.value.query.trim())
                    .first()
                    .filter { folder -> !folder.deleted }
                    .sortedBy { folder -> folder.name.lowercase() }
            )
        }
    }

    fun clear() {
        _screenStateStream.update {
            it.copy(
                query = "",
                resultCoasters = emptyList(),
                resultFolders = emptyList()
            )
        }
    }
}

data class SearchScreenState(
    val query: String = "",
    val resultCoasters: List<Coaster> = emptyList(),
    val resultFolders: List<Folder> = emptyList()
)