package uk.ac.tees.mad.lendabook.presentation.components.textfields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.AppLabelText
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    supportingText: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    AppLabelText(label = stringResource(R.string.name))
    Spacer(modifier = Modifier.height(Dimen.SpacerExtraSmall))
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier,
        placeholder = {
            Text(text = stringResource(R.string.name_desc))
        },
        supportingText = supportingText,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        shape = MaterialTheme.shapes.small,
    )
}