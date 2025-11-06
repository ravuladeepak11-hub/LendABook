package uk.ac.tees.mad.lendabook.presentation.screens.login

data class LoginUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: List<String>? = null
)