package cz.tonda2.podtacky.features.coaster.presentation.edit

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.presentation.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

class EditViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val id: Long
        get() = savedStateHandle[Screen.EditScreen.ID] ?: -1L

    private val _screenStateStream = MutableStateFlow(EditScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            if (id != -1L) {
                val coaster = coasterRepository.getCoasterById(id.toString())
                _screenStateStream.update {
                    it.copy(
                        title = "Upravit podtácek",
                        oldCoaster = coaster,
                        brewery = coaster.brewery,
                        description = coaster.description,
                        date = coaster.dateAdded,
                        city = coaster.city,
                        count = coaster.count.toString(),
                        frontUri = coaster.frontUri,
                        backUri = coaster.backUri
                    )
                }
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
            old.count.toString() == _screenStateStream.value.count &&
            old.frontUri == _screenStateStream.value.frontUri &&
            old.backUri == _screenStateStream.value.backUri
        ) return

        _screenStateStream.update {
            it.copy(
                state = ScreenState.Loading
            )
        }
        viewModelScope.launch {
            coasterRepository.addCoaster(
                Coaster(
                    uid = UUID.randomUUID().toString(),
                    brewery = _screenStateStream.value.brewery.trim(),
                    description = _screenStateStream.value.description.trim(),
                    dateAdded = _screenStateStream.value.date,
                    city = _screenStateStream.value.city.trim(),
                    count = _screenStateStream.value.count.toInt(),
                    frontUri = _screenStateStream.value.frontUri,
                    backUri = _screenStateStream.value.backUri,
                    uploaded = false,
                    deleted = false
                )
            )

            if (old != null) {
                if (old.uploaded) {
                    coasterRepository.markDeleted(old.coasterId.toString())
                }
                else {
                    coasterRepository.deleteCoaster(old)
                }
            }
        }
        _screenStateStream.update {
            it.copy(
                state = ScreenState.Fill
            )
        }
    }

    fun deletePicture(uri: Uri, context: Context) {
        if (uri != Uri.EMPTY) {
            if (screenStateStream.value.frontUri == uri) {
                updateFrontUri(Uri.EMPTY)
            } else {
                updateBackUri(Uri.EMPTY)
            }

            try {
                context.contentResolver.delete(uri, null, null)
                Log.d("Deleting", "Deleted $uri")
            } catch (e: Exception) {
                Log.e("Deleting", "Error deleting $uri")
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

    fun updateFrontUri(uri: Uri) {
        _screenStateStream.update {
            it.copy(
                frontUri = uri
            )
        }
    }

    fun updateBackUri(uri: Uri) {
        _screenStateStream.update {
            it.copy(
                backUri = uri
            )
        }
    }
}

data class EditScreenState(
    val title: String = "Přidat podtácek",
    val oldCoaster: Coaster? = null,
    val brewery: String = "",
    val description: String = "",
    val date: String = Calendar.getInstance().time.toString("dd.MM.yyy"),
    val city: String = "",
    val count: String = "1",
    val frontUri: Uri = Uri.EMPTY,
    val backUri: Uri = Uri.EMPTY,
    val state: ScreenState = ScreenState.Fill
)

fun Date.toString(format: String): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this)
}