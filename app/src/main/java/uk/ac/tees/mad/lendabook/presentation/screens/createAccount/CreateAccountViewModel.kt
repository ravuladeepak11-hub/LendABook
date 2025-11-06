package uk.ac.tees.mad.lendabook.presentation.screens.createAccount

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
class CreateAccountViewModel @Inject constructor(
    private val authRepo: FirebaseAuthRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _createAccountUiState = MutableStateFlow(CreateAccountUiState())
    val createAccountUiState = _createAccountUiState.asStateFlow()

    private val _createAccountNavigation = MutableSharedFlow<CreateAccountNavigation>()
    val createAccountNavigation = _createAccountNavigation.asSharedFlow()

    fun onEvent(event: CreateAccountUiEvent) {
        when (event) {
            is CreateAccountUiEvent.NameChanged -> {
                _createAccountUiState.update {
                    it.copy(
                        name = event.name,
                        nameError = if (event.name.length < 3) "Please at least 3 char enter" else null
                    )
                }
            }

            is CreateAccountUiEvent.EmailChanged -> {
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _createAccountUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (!isEmailValid) "Please enter a valid email" else null
                    )
                }
            }

            is CreateAccountUiEvent.PasswordChange -> {
                val error = getValidPassword(event.password)
                _createAccountUiState.update {
                    it.copy(
                        password = event.password,
                        passwordError = error
                    )
                }
            }

            CreateAccountUiEvent.CreateAccountClicked -> {
                createAccount()
            }
        }
    }

    private fun createAccount() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            authRepo.signUp(
                email = createAccountUiState.value.email,
                password = createAccountUiState.value.password
            ).onSuccess {
                _uiState.value = UiState.Success("Create Account Success!")
                _createAccountNavigation.emit(CreateAccountNavigation.Home)
                _uiState.value = UiState.Idle
            }.onFailure { throwable ->
                _uiState.value =
                    UiState.Error(throwable.localizedMessage ?: "Create Account Failed!")
                _uiState.value = UiState.Idle
            }
        }
    }
}