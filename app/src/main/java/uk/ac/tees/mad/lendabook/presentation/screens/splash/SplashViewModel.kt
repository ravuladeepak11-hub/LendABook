package uk.ac.tees.mad.lendabook.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import uk.ac.tees.mad.lendabook.presentation.navigation.DashboardRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.LoginRoute
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepo: FirebaseAuthRepo,
) : ViewModel() {

    private val _splashNav = MutableSharedFlow<SplashNav>()
    val splashNav = _splashNav.asSharedFlow()

    init {
        loggedIn()
    }

    private fun loggedIn() {
        viewModelScope.launch {
            authRepo.checkAuthStatus()
                .collect { isLoggedIn ->
                    _splashNav.emit(
                        if (isLoggedIn) SplashNav.Dashboard
                        else SplashNav.Login
                    )
                }
        }
    }


}