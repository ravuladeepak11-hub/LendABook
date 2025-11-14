package uk.ac.tees.mad.lendabook.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.lendabook.presentation.components.AppIcon

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background
    )
    AppIcon(
        iconSize = 80.dp,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(gradientBackground)),
        contentAlignment = Alignment.Center,
    )
}

@Preview
@Composable
fun showSplash(){
    SplashScreen()
}