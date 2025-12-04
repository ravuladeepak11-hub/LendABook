package uk.ac.tees.mad.lendabook.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.lendabook.presentation.components.AppIcon
import uk.ac.tees.mad.lendabook.presentation.navigation.DashboardRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.LoginRoute

@Composable
fun SplashScreen(navController: NavHostController) {

    val viewModel: SplashViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.splashNav.collect { nav ->
            when (nav) {
                SplashNav.Dashboard -> {
                    navController.navigate(DashboardRoute)
                }

                SplashNav.Login -> {
                    navController.navigate(LoginRoute)
                }
            }
        }
    }

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

