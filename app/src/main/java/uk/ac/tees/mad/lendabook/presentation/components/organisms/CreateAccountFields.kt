package uk.ac.tees.mad.lendabook.presentation.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.lendabook.presentation.components.molecules.textfields.EmailTextField
import uk.ac.tees.mad.lendabook.presentation.components.molecules.textfields.NameTextField
import uk.ac.tees.mad.lendabook.presentation.components.molecules.textfields.PasswordTextField
import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountUiEvent
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun CreateAccountFields(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        NameTextField(
            value = name,
            onValueChange = {
                onNameChange(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerSmall))

        EmailTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerSmall))

        PasswordTextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}