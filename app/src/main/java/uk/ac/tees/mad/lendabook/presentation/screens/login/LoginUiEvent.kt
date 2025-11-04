package uk.ac.tees.mad.lendabook.presentation.screens.login

import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountUiEvent

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChange(val password: String) : LoginUiEvent()
    object LoginClicked : LoginUiEvent()
}

sealed interface LoginNavigationEvent {
    object ForgetScreen : LoginNavigationEvent
    object HomeScreen : LoginNavigationEvent
}