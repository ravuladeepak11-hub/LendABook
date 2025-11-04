package uk.ac.tees.mad.lendabook.presentation.screens.createAccount

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateAccountViewModel : ViewModel() {

    private val _createAccountUiState = MutableStateFlow(CreateAccountUiState())
    val createAccountUiState = _createAccountUiState.asStateFlow()

    private val _createAccountNavEvent = MutableSharedFlow<CreateAccountNavigationEvent>()
    val createAccountNavEvent = _createAccountNavEvent.asSharedFlow()

    fun onEvent(event: CreateAccountUiEvent) {
        when (event) {
            is CreateAccountUiEvent.NameChanged -> {
                _createAccountUiState.value = createAccountUiState.value.copy(name = event.name)
            }

            is CreateAccountUiEvent.EmailChanged -> {
                _createAccountUiState.value = createAccountUiState.value.copy(email = event.email)
            }

            is CreateAccountUiEvent.PasswordChange -> {
                _createAccountUiState.value = createAccountUiState.value.copy(password = event.password)
            }

            CreateAccountUiEvent.CreateAccountClicked -> {
                /*TODO*/
            }
        }
    }

}