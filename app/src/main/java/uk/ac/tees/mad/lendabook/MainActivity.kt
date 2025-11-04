package uk.ac.tees.mad.lendabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.lendabook.presentation.screens.ForgetScreen
import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountScreen
import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountViewModel
import uk.ac.tees.mad.lendabook.presentation.screens.login.LoginScreen
import uk.ac.tees.mad.lendabook.presentation.screens.splash.SplashScreen
import uk.ac.tees.mad.lendabook.ui.theme.LendABookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: CreateAccountViewModel by viewModels()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LendABookTheme {

            }
        }
    }
}
