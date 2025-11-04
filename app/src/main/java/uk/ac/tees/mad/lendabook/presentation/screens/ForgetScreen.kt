package uk.ac.tees.mad.lendabook.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import uk.ac.tees.mad.lendabook.presentation.components.templates.ForgetTemplate
import uk.ac.tees.mad.lendabook.presentation.screens.login.LoginContent
import uk.ac.tees.mad.lendabook.utils.Dimen

@Composable
fun ForgetScreen(modifier: Modifier = Modifier) {
    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientBackground)),
    ) {
        ForgetContent()
    }

}

@Composable
fun ForgetContent(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = Dimen.PaddingMedium),
        verticalArrangement = Arrangement.Center
    ) {
        ForgetTemplate()
    }
}