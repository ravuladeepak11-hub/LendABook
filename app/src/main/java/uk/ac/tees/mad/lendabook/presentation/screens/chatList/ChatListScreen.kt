package uk.ac.tees.mad.lendabook.presentation.screens.chatList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.ChatRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.MessageRoute


fun NavGraphBuilder.messageRoute(navController: NavHostController) = composable<MessageRoute>() {
    ChatListScreen(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavHostController) {
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
        ChatListContent(paddingValues = paddingValues)
    }
}

@Composable
fun ChatListContent(paddingValues: PaddingValues) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "LendABook – Chat List Screen")
@Composable
fun ChatListScreenPreview() {
    DashboardScaffold(
        navController = rememberNavController(),
        topBar = {
            TopAppBar(
                title = { Text("Messages") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your conversations will appear here",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Start lending or borrowing books to begin chatting!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}