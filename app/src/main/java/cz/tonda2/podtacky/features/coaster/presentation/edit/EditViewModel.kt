package cz.tonda2.podtacky.features.coaster.presentation.edit

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.presentation.Screen
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.presentation.ScreenState
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

    var coasterUiState by mutableStateOf(EditScreenState())
        private set

    init {
        viewModelScope.launch {
            if (id != -1L) {
                val coaster = coasterRepository.getCoasterById(id.toString())

                coasterUiState = EditScreenState(
                    title = "Upravit podtácek",
                    oldCoaster = coaster,
                    brewery = coaster?.brewery ?: "",
                    description = coaster?.description ?: "",
                    date = coaster?.dateAdded ?: "",
                    city = coaster?.city ?: "",
                    count = coaster?.count.toString(),
                    frontUri = coaster?.frontUri ?: Uri.EMPTY,
                    backUri = coaster?.backUri ?: Uri.EMPTY
                )
            }
        }
    }

    fun saveEdit() {
        val old = coasterUiState.oldCoaster
        if (
            old?.brewery == coasterUiState.brewery &&
            old.description == coasterUiState.description &&
            old.dateAdded == coasterUiState.date &&
            old.city == coasterUiState.city &&
            old.count.toString() == coasterUiState.count &&
            old.frontUri == coasterUiState.frontUri &&
            old.backUri == coasterUiState.backUri
        ) return

        coasterUiState = coasterUiState.copy(
            state = ScreenState.Loading
        )

        viewModelScope.launch {
            coasterRepository.addCoaster(
                Coaster(
                    uid = UUID.randomUUID().toString(),
                    brewery = coasterUiState.brewery.trim(),
                    description = coasterUiState.description.trim(),
                    dateAdded = coasterUiState.date,
                    city = coasterUiState.city.trim(),
                    count = coasterUiState.count.toIntOrNull() ?: 1,
                    frontUri = coasterUiState.frontUri,
                    backUri = coasterUiState.backUri,
                    uploaded = false,
                    deleted = false
                )
            )

            if (old != null) {
                if (old.uploaded) {
                    coasterRepository.markDeleted(old.coasterId.toString())
                } else {
                    coasterRepository.deleteCoaster(old)
                }
            }
        }
        coasterUiState = coasterUiState.copy(
            state = ScreenState.Fill
        )
    }

    fun deletePicture(uri: Uri, context: Context) {
        if (uri != Uri.EMPTY) {
            if (coasterUiState.frontUri == uri) {
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
        coasterUiState = coasterUiState.copy(
            brewery = brewery
        )
    }

    fun updateDescription(description: String) {
        coasterUiState = coasterUiState.copy(
            description = description
        )
    }

    fun updateDate(date: String) {
        coasterUiState = coasterUiState.copy(
            date = date
        )
    }

    fun updateCity(city: String) {
        coasterUiState = coasterUiState.copy(
            city = city
        )
    }

    fun updateCount(count: String) {
        if (count.isNotEmpty() && count.toIntOrNull() == null) return

        coasterUiState = coasterUiState.copy(
            count = count
        )
    }

    fun updateFrontUri(uri: Uri) {
        coasterUiState = coasterUiState.copy(
            frontUri = uri
        )
    }

    fun updateBackUri(uri: Uri) {
        coasterUiState = coasterUiState.copy(
            backUri = uri
        )
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