package uk.ac.tees.mad.lendabook.presentation.components.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.presentation.navigation.BrowseBookRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.MessageRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.SettingRoute

data class BottomBarItem(
    val route: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val iconDescription: String,
)

val bottomBarItemList = listOf(
    BottomBarItem(
        route = BrowseBookRoute,
        label = "BrowseBook",
        selectedIcon = Icons.Filled.Book,
        unSelectedIcon = Icons.Outlined.Book,
        iconDescription = "Book Icon"
    ),
    BottomBarItem(
        route = MessageRoute,
        label = "Message",
        selectedIcon = Icons.Filled.Message,
        unSelectedIcon = Icons.Outlined.Message,
        iconDescription = "Message Icon"
    ),
    BottomBarItem(
        route = SettingRoute,
        label = "Setting",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
        iconDescription = "Setting Icon"
    )
)


@Composable
fun DashboardBottomAppBar(
    navController: NavController,
    bottomBarItemList: List<BottomBarItem>,
) {
    val scope = rememberCoroutineScope()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        bottomBarItemList.forEach { item ->
            val isSelected = remember(currentDestination) {
                currentDestination == item.route::class.qualifiedName
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    scope.launch {
                        if (!isSelected) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = item.iconDescription
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}