package uk.ac.tees.mad.lendabook.presentation.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.domain.model.User
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepo: FirebaseAuthRepo,
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()
    private val _navigation = MutableSharedFlow<SettingNavigation>()
    val navigation = _navigation.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        getUser()
    }

    fun onEvent(event: SettingUiEvent) {
        when (event) {
            SettingUiEvent.DeleteAccountClick ->{
                signOut()
            }
            SettingUiEvent.SignOutClick -> {
                deleteAccount()
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            authRepo.getUser()
                .onSuccess {
                    _user.value = it
                }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepo.signOut()
            result.onSuccess {
                _uiState.value = UiState.Success("Signed out")
                _navigation.emit(SettingNavigation.SIGN_OUT)
            }
            result.onFailure {
                _uiState.value = UiState.Error("Signed out failed!")
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepo.deleteUser()
            result.onSuccess {
                _uiState.value = UiState.Success(" Account deleted")
                _navigation.emit(SettingNavigation.DELETE_ACCOUNT)
            }
            result.onFailure {
                _uiState.value = UiState.Error("Account deleted failed!")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }

}