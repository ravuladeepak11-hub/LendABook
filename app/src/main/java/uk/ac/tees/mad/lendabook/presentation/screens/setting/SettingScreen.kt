package uk.ac.tees.mad.lendabook.presentation.screens.setting

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.MessageRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.SettingRoute

fun NavGraphBuilder.settingRoute(navController: NavHostController) = composable<SettingRoute>() {
    SettingScreen(navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController) {
    DashboardScaffold(
        navController = navController,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.setting)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        SettingContent(paddingValues)
    }
}

@Composable
fun SettingContent(paddingValues: PaddingValues) {

}


@Preview
@Composable
fun setting(){
    val nav = rememberNavController()
    SettingScreen(nav)
}