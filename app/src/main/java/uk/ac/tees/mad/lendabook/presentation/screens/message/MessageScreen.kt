package uk.ac.tees.mad.lendabook.presentation.screens.message

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.ChatRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.MessageRoute
import uk.ac.tees.mad.lendabook.presentation.screens.chat.ChatScreen


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
                title = { Text(stringResource(id = R.string.message)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Button(
            onClick = {
                navController.navigate(ChatRoute)
            },
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            Text("Chats")
        }
        MessageContent(paddingValues = paddingValues)
    }
}

@Composable
fun MessageContent(paddingValues: PaddingValues) {

}