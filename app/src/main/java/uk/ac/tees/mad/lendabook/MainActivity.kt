package uk.ac.tees.mad.lendabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.lendabook.presentation.navigation.AppNavGraph
import uk.ac.tees.mad.lendabook.ui.theme.LendABookTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LendABookTheme {
                AppNavGraph()
            }
        }
    }
}
