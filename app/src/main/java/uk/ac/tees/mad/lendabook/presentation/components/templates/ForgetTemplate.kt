package uk.ac.tees.mad.lendabook.presentation.components.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.atomic.AppTitleText
import uk.ac.tees.mad.lendabook.presentation.components.molecules.buttons.ForgetButton
import uk.ac.tees.mad.lendabook.presentation.components.molecules.textfields.EmailTextField
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun ForgetTemplate(modifier: Modifier = Modifier) {

    Column() {
        AppTitleText(
            title = stringResource(R.string.forget_password)
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        EmailTextField(
            value = "",
            onValueChange = {

            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerSmall))
        ForgetButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }

}