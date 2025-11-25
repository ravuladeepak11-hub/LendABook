package uk.ac.tees.mad.lendabook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.lendabook.presentation.screens.addbook.AddBookScreen
import uk.ac.tees.mad.lendabook.presentation.screens.bookDetail.BookDetailScreen
import uk.ac.tees.mad.lendabook.presentation.screens.browseBook.browseBookRoute
import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountScreen
import uk.ac.tees.mad.lendabook.presentation.screens.forget.ForgetScreen
import uk.ac.tees.mad.lendabook.presentation.screens.login.LoginScreen
import uk.ac.tees.mad.lendabook.presentation.screens.message.messageRoute
import uk.ac.tees.mad.lendabook.presentation.screens.setting.settingRoute
import uk.ac.tees.mad.lendabook.presentation.screens.splash.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DashboardRoute
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

        navigation<DashboardRoute>(startDestination = BrowseBookRoute) {
            browseBookRoute(navController)
            messageRoute(navController)
            settingRoute(navController)
        }

        composable<AddBookRoute> {
            AddBookScreen(navController)
        }

        composable<BookDetailRoute> {
            BookDetailScreen(navController)
        }
    }

}