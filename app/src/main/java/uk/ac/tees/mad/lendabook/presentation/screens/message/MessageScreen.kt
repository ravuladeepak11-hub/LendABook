package uk.ac.tees.mad.lendabook.presentation.screens.message

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.MessageRoute


fun NavGraphBuilder.messageRoute(navController: NavHostController) = composable<MessageRoute>() {
    MessageScreen(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(navController: NavHostController) {
    DashboardScaffold(
        navController = navController,
        topBar = {
            TopAppBar(
                title = { Text("Message") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        MessageContent(paddingValues)
    }
}

@Composable
fun MessageContent(paddingValues: PaddingValues) {

}