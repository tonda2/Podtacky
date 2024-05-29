package cz.cvut.fit.podtacky.features.coaster.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(SearchScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    fun updateQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                _screenStateStream.update {
                    it.copy(
                        query = query
                    )
                }
            }
        }
    }

    suspend fun searchCoasters() {
        _screenStateStream.update {
            it.copy(
                query = _screenStateStream.value.query,
                result = coasterRepository.searchCoasterByBreweryName(_screenStateStream.value.query).first()
            )
        }
    }

    fun clear() {
        _screenStateStream.update {
            it.copy(
                query = "",
                result = emptyList()
            )
        }
    }
}

data class SearchScreenState(
    val query: String = "",
    val result: List<Coaster> = emptyList()
)