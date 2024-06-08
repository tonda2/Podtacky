package cz.cvut.fit.podtacky.features.coaster.presentation.add

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.features.coaster.data.CoasterRepository
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import cz.cvut.fit.podtacky.features.coaster.presentation.ScreenState
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
                    frontUri = _screenStateStream.value.frontUri,
                    backUri = _screenStateStream.value.backUri
                )
            )
            _screenStateStream.update {
                it.copy(
                    state = ScreenState.Fill
                )
            }
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

    fun deletePictures(context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver

            try {
                var delCount = 0
                if (screenStateStream.value.frontUri != Uri.EMPTY) {
                    delCount += contentResolver.delete(screenStateStream.value.frontUri, null, null)
                }
                if (screenStateStream.value.backUri != Uri.EMPTY) {
                    delCount += contentResolver.delete(screenStateStream.value.backUri, null, null)
                }
                Log.d("Deleting", "Deleted $delCount images")
            } catch (e: Exception) {
                Log.e("Deleting", e.toString())
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

data class AddScreenState(
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