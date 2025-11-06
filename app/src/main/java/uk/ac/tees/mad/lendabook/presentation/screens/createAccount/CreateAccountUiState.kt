package uk.ac.tees.mad.lendabook.presentation.screens.createAccount


data class CreateAccountUiState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError:List<String>? = null,
)