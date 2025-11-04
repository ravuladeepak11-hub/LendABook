package uk.ac.tees.mad.lendabook.presentation.components.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.lendabook.presentation.components.atomic.AppTitleText
import uk.ac.tees.mad.lendabook.presentation.components.molecules.buttons.LoginButton
import uk.ac.tees.mad.lendabook.presentation.components.organisms.LoginTextFields
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun LoginTemplate(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        AppTitleText(
            title = "Welcome Back!"
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        LoginTextFields(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {}
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        LoginButton(
            onClick = {
                /*TODO*/
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}