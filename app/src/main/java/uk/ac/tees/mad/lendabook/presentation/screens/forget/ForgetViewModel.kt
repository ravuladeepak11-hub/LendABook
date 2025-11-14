package uk.ac.tees.mad.lendabook.presentation.screens.forget

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel @Inject constructor(
    private val authRepo: FirebaseAuthRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _forgetUiState = MutableStateFlow(ForgetUiState())
    val forgetUiState = _forgetUiState.asStateFlow()

    private val _forgetNavigation = MutableSharedFlow<ForgetNavigation>()
    val forgetNavigation = _forgetNavigation.asSharedFlow()

    fun onEvent(event: ForgetUiEvent) {
        when (event) {
            is ForgetUiEvent.EmailChanged -> {
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _forgetUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (isEmailValid) "Please enter a valid email" else null
                    )
                }
            }

            ForgetUiEvent.SendRestClick -> {
                forgetPassword()
            }

            ForgetUiEvent.SignInClick -> {
                viewModelScope.launch {
                    _forgetNavigation.emit(ForgetNavigation.Login)
                }
            }
        }
    }

    fun forgetPassword() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepo.forgetPassword(
                email = forgetUiState.value.email
            )
            result.onSuccess {
                _uiState.value = UiState.Success("Password reset link sent to ${forgetUiState.value.email}")
                _forgetNavigation.emit(ForgetNavigation.Login)
            }

            result.onFailure {
                _uiState.value = UiState.Error(
                    it.localizedMessage ?: "Unable to send reset link. Please check the email."
                )
            }
        }
    }

    fun restUiState() {
        _uiState.value = UiState.Idle
    }

}