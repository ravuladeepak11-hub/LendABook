package uk.ac.tees.mad.lendabook.presentation.navigation

import kotlinx.serialization.Serializable

//Starter Routes
@Serializable
object SplashRoute

@Serializable
object LoginRoute

@Serializable
object CreateAccountRoute

@Serializable
object ForgetRoute


//Main Routes
@Serializable
object DashboardRoute

@Serializable
object BrowseBookRoute

@Serializable
object AddBookRoute

@Serializable
object MessageRoute

@Serializable
object SettingRoute


@Serializable
data class BookDetailRoute(val isbn: String)

@Serializable
object ChatRoute