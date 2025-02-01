package cz.tonda2.podtacky.features.profile.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.tonda2.podtacky.core.data.BackupManager
import cz.tonda2.podtacky.core.data.ImportManager
import cz.tonda2.podtacky.features.coaster.data.CoasterRepository
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.profile.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val backupManager: BackupManager,
    private val importManager: ImportManager,
    private val coasterRepository: CoasterRepository
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileScreenState())
    val profileUiState: StateFlow<ProfileScreenState> = _profileUiState

    init {
        viewModelScope.launch {
            combine(
                userRepository.userStream,
                coasterRepository.getUndeletedCoastersList()
            ) { user, coasters ->
                ProfileScreenState(
                    id = user?.id,
                    name = user?.name,
                    coasters = coasters,
                    downloading = _profileUiState.value.downloading,
                    downloadCount = _profileUiState.value.downloadCount
                )
            }.collect { newState ->
                _profileUiState.value = newState
            }
        }
    }

    fun logOut(context: Context) {
        userRepository.logOut(context)
    }

    fun backup() {
        viewModelScope.launch {
            backupManager.createBackup()
        }
    }

    fun import(context: Context) {
        viewModelScope.launch {
            _profileUiState.update {
                it.copy(downloading = true, downloadCount = 0)
            }

            importManager.importBackup(context) {
                _profileUiState.update {
                    it.copy(downloadCount = _profileUiState.value.downloadCount + 1)
                }
            }

            _profileUiState.update {
                it.copy(downloading = false, downloadCount = 0)
            }
        }
    }
}

data class ProfileScreenState(
    val id: String? = null,
    val name: String? = null,
    val downloading: Boolean = false,
    val downloadCount: Int = 0,
    val coasters: List<Coaster> = emptyList()
)
