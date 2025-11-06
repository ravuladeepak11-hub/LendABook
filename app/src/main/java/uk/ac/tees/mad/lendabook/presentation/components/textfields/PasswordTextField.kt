package uk.ac.tees.mad.lendabook.presentation.components.textfields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.AppLabelText
import uk.ac.tees.mad.lendabook.ui.theme.LendABookTheme
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    AppLabelText(label = stringResource(R.string.password))
    Spacer(modifier = Modifier.height(Dimen.SpacerExtraSmall))
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier,
        maxLines = 1,
        placeholder = {
            Text(text = stringResource(R.string.password_desc))
        },
        supportingText = supportingText,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        shape = MaterialTheme.shapes.small,
    )
}

@Preview(showBackground = true)
@Composable
private fun Testing() {
    LendABookTheme() {
        PasswordTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}