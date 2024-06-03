package cz.cvut.fit.podtacky.features.fact.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.features.fact.data.FactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FactViewModel(
    private val factRepository: FactRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(FactScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    fun getFact() {
        viewModelScope.launch {
            val fact = factRepository.getFact()
            _screenStateStream.update {
                it.copy(fact = fact)
            }
        }
    }
}

data class FactScreenState(
    val fact: String = ""
)