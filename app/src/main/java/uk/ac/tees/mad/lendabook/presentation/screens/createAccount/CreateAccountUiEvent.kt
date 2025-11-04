package uk.ac.tees.mad.lendabook.presentation.screens.createAccount

sealed class CreateAccountUiEvent {
    data class NameChanged(val name: String) : CreateAccountUiEvent()
    data class EmailChanged(val email: String) : CreateAccountUiEvent()
    data class PasswordChange(val password: String) : CreateAccountUiEvent()
    object CreateAccountClicked : CreateAccountUiEvent()
}

sealed interface CreateAccountNavigationEvent {
    object ForgetScreen : CreateAccountNavigationEvent
    object HomeScreen : CreateAccountNavigationEvent
}