package cz.cvut.fit.podtacky.features.coaster.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.core.presentation.Screen
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import cz.cvut.fit.podtacky.features.coaster.presentation.ScreenState
import cz.cvut.fit.podtacky.features.coaster.presentation.add.toString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class EditViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.EditScreen.ID] ?: 1L

    private val _screenStateStream = MutableStateFlow(EditScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            val coaster = coasterRepository.getCoasterById(id.toString())
            _screenStateStream.update {
                it.copy(
                    oldCoaster = coaster,
                    brewery = coaster.brewery,
                    description = coaster.description,
                    date = coaster.dateAdded,
                    city = coaster.city,
                    count = coaster.count.toString(),
                )
            }
        }
    }

    fun saveEdit() {
        val old = _screenStateStream.value.oldCoaster
        if (
            old?.brewery == _screenStateStream.value.brewery &&
            old.description == _screenStateStream.value.description &&
            old.dateAdded == _screenStateStream.value.date &&
            old.city == _screenStateStream.value.city &&
            old.count.toString() == _screenStateStream.value.count
        ) return

        _screenStateStream.update {
            it.copy(
                state = ScreenState.Loading
            )
        }
        viewModelScope.launch {
            coasterRepository.deleteCoaster(
                old!!
            )
            coasterRepository.addCoaster(
                Coaster(
                    brewery = _screenStateStream.value.brewery,
                    description = _screenStateStream.value.description,
                    dateAdded = _screenStateStream.value.date,
                    city = _screenStateStream.value.city,
                    count = _screenStateStream.value.count.toInt(),
                )
            )
        }
        _screenStateStream.update {
            it.copy(
                state = ScreenState.Fill
            )
        }
    }

    fun updateBrewery(brewery: String) {
        _screenStateStream.update {
            it.copy(
                brewery = brewery
            )
        }
    }
    fun updateDescription(description: String) {
        _screenStateStream.update {
            it.copy(
                description = description
            )
        }
    }
    fun updateDate(date: String) {
        _screenStateStream.update {
            it.copy(
                date = date
            )
        }
    }
    fun updateCity(city: String) {
        _screenStateStream.update {
            it.copy(
                city = city
            )
        }
    }
    fun updateCount(count: String) {
        _screenStateStream.update {
            it.copy(
                count = count
            )
        }
    }
}

data class EditScreenState(
    val oldCoaster: Coaster? = null,
    val brewery: String = "",
    val description: String = "",
    val date: String = Calendar.getInstance().time.toString("dd.MM.yyy"),
    val city: String = "",
    val count: String = "1",
    val state: ScreenState = ScreenState.Fill
)