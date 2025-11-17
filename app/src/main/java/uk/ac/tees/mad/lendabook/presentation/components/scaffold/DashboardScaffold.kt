package uk.ac.tees.mad.lendabook.presentation.components.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import uk.ac.tees.mad.lendabook.presentation.components.bottomBar.DashboardBottomAppBar
import uk.ac.tees.mad.lendabook.presentation.components.bottomBar.bottomBarItemList

@Composable
fun DashboardScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = {
            DashboardBottomAppBar(
                navController = navController,
                bottomBarItemList = bottomBarItemList
            )
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = snackbarHost,
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        content(innerPadding)
    }
}