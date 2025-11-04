package uk.ac.tees.mad.lendabook.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.lendabook.presentation.components.atomic.AppIcon
import uk.ac.tees.mad.lendabook.presentation.components.molecules.buttons.CreateAccountButton
import uk.ac.tees.mad.lendabook.presentation.components.templates.LoginTemplate
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun LoginScreen() {

    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientBackground)),
    ) {
        LoginContent()
    }

}

@Composable
fun LoginContent() {

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = Dimen.PaddingMedium),
        verticalArrangement = Arrangement.Center
    )
    {
        AppIcon(
            iconSize = 48.dp,
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerExtraLarge))
        LoginTemplate()

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Don't have an account?")
        }
        CreateAccountButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
    }

}