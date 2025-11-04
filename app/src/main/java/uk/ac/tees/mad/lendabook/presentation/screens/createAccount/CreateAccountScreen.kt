package uk.ac.tees.mad.lendabook.presentation.screens.createAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import uk.ac.tees.mad.lendabook.presentation.components.templates.CreateAccountTemplate
import uk.ac.tees.mad.lendabook.presentation.screens.login.LoginContent
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun CreateAccountScreen(viewModel: CreateAccountViewModel) {
    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientBackground)),
    ) {
        CreateAccountContent(viewModel = viewModel)
    }

}

@Composable
fun CreateAccountContent(viewModel: CreateAccountViewModel) {

    val createAccountUiState by viewModel.createAccountUiState.collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = Dimen.PaddingMedium),
        verticalArrangement = Arrangement.Center
    ) {
        CreateAccountTemplate()
    }
}