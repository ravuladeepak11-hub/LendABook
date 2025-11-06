package uk.ac.tees.mad.lendabook.presentation.screens.login

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
import uk.ac.tees.mad.lendabook.utils.getValidPassword
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: FirebaseAuthRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val _loginNavigation = MutableSharedFlow<LoginNavigation>()
    val loginNavigation = _loginNavigation.asSharedFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _loginUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (isEmailValid) "Please enter a valid email" else null
                    )
                }
            }

            is LoginUiEvent.PasswordChange -> {
                val errors = getValidPassword(event.password)
                _loginUiState.update {
                    it.copy(
                        password = event.password,
                        passwordError = errors
                    )
                }
            }

            LoginUiEvent.LoginClicked -> {
                login()
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            authRepo.logIn(
                email = loginUiState.value.email,
                password = loginUiState.value.password
            ).onSuccess {
                _uiState.value = UiState.Success("Login Successfully")
                _loginNavigation.emit(LoginNavigation.Home)
                _uiState.value = UiState.Idle
            }.onFailure { throwable ->
                _uiState.value = UiState.Error("$throwable: Login Failed")
                _uiState.value = UiState.Idle
            }
        }
    }


}