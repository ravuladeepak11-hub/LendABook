package uk.ac.tees.mad.lendabook.presentation.components.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.lendabook.presentation.components.atomic.AppTitleText
import uk.ac.tees.mad.lendabook.presentation.components.molecules.buttons.CreateAccountButton
import uk.ac.tees.mad.lendabook.presentation.components.organisms.CreateAccountFields
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun CreateAccountTemplate(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
    ) {
        AppTitleText(
            title = "Join LendABook"
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        CreateAccountFields(
            name = "",
            onNameChange = {},
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {}
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        CreateAccountButton(
            onClick = {
                /*TODO*/
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}