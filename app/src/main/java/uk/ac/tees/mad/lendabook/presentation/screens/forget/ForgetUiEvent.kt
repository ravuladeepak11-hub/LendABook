package uk.ac.tees.mad.lendabook.presentation.screens.forget

sealed class ForgetUiEvent {
    data class EmailChanged(val email: String) : ForgetUiEvent()
    object SendRestClick : ForgetUiEvent()
    object SignInClick : ForgetUiEvent()
}

enum class ForgetNavigation {
    Login
}