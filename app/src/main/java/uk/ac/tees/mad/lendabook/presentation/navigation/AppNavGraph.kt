package uk.ac.tees.mad.lendabook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.lendabook.presentation.screens.forget.ForgetScreen
import uk.ac.tees.mad.lendabook.presentation.screens.addbook.AddBookScreen
import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountScreen
import uk.ac.tees.mad.lendabook.presentation.screens.home.HomeScreen
import uk.ac.tees.mad.lendabook.presentation.screens.login.LoginScreen
import uk.ac.tees.mad.lendabook.presentation.screens.splash.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AddBookRoute
    ) {
        composable<SplashRoute> {
            SplashScreen()
        }
        composable<CreateAccountRoute> {
            CreateAccountScreen(navController)
        }

        composable<LoginRoute> {
            LoginScreen(navHostController = navController)
        }

        composable<ForgetRoute> {
            ForgetScreen(navController)
        }

        composable<HomeRoute> {
            HomeScreen()
        }

        composable<AddBookRoute> {
            AddBookScreen()
        }
    }

}