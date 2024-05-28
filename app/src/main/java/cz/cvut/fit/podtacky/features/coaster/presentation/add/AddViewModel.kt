package cz.cvut.fit.podtacky.features.coaster.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddViewModel(
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(AddScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    fun save() {
        _screenStateStream.update {
            it.copy(
                state = ScreenState.Loading
            )
        }
        viewModelScope.launch {
            coasterRepository.addCoaster(
                Coaster(
                    brewery = _screenStateStream.value.brewery,
                    description = _screenStateStream.value.description,
                    dateAdded = _screenStateStream.value.date,
                    city = _screenStateStream.value.city,
                    count = _screenStateStream.value.count.toInt(),
                )
            )
            _screenStateStream.update {
                it.copy(
                    state = ScreenState.Fill
                )
            }
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

data class AddScreenState(
    val brewery: String = "",
    val description: String = "",
    val date: String = Calendar.getInstance().time.toString("dd.MM.yyy"),
    val city: String = "",
    val count: String = "1",
    val state: ScreenState = ScreenState.Fill
)

enum class ScreenState {
    Loading,
    Fill
}

fun Date.toString(format: String): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this)
}