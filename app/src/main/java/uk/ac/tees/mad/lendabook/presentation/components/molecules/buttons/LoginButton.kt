package uk.ac.tees.mad.lendabook.presentation.components.molecules.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.ui.theme.LendABookTheme

@Composable
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(R.string.login))
    }
}

@Preview(showBackground = true)
@Composable
private fun Testing() {
    LendABookTheme() {
        LoginButton(
            onClick = {

            }
        )
    }
}