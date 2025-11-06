package uk.ac.tees.mad.lendabook.presentation.screens.login

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()
    data class PasswordChange(val password: String) : LoginUiEvent()
    object LoginClicked : LoginUiEvent()
}

enum class LoginNavigation {
    Forget, Home, CreateAccount
}