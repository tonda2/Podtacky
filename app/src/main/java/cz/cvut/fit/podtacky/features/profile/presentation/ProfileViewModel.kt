package cz.cvut.fit.podtacky.features.profile.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.podtacky.core.data.BackupManager
import cz.cvut.fit.podtacky.features.profile.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _screenStateStream = MutableStateFlow(ProfileScreenState())
    val screenStateStream = _screenStateStream.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.userStream.collect { user ->
                _screenStateStream.update {
                    it.copy(id = user?.id, name = user?.name)
                }
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
}

data class ProfileScreenState(
    val id: String? = null,
    val name: String? = null
)
