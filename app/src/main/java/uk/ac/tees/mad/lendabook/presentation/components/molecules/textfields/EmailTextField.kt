package uk.ac.tees.mad.lendabook.presentation.components.molecules.textfields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.atomic.AppLabelText
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppLabelText(label = stringResource(R.string.email))
    Spacer(modifier = Modifier.height(Dimen.SpacerExtraSmall))
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier,
        maxLines = 1,
        placeholder = {
            Text(text = stringResource(R.string.email_desc))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        shape = MaterialTheme.shapes.small,
    )
}