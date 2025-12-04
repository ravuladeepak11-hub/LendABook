package uk.ac.tees.mad.lendabook.presentation.screens.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.CreateAccountRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.LoginRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.SettingRoute
import uk.ac.tees.mad.lendabook.utils.showToast

fun NavGraphBuilder.settingRoute(navController: NavHostController) = composable<SettingRoute>() {
    SettingScreen(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController) {

    val viewModel: SettingViewModel = hiltViewModel()

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                val successMessage = (uiState as UiState.Success).message
                context.showToast(successMessage)
            }

            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).message
                context.showToast(errorMessage)
            }

            else -> Unit
        }
    }


    LaunchedEffect(Unit) {
        viewModel.navigation.collect { destination ->
            when (destination) {

                SettingNavigation.SIGN_OUT -> {
                    navController.navigate(LoginRoute)
                    viewModel.resetUiState()
                }

                SettingNavigation.DELETE_ACCOUNT -> {
                    navController.navigate(CreateAccountRoute)
                    viewModel.resetUiState()
                }
            }
        }
    }

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
        SettingContent(paddingValues, viewModel)
    }
}

@Composable
fun SettingContent(paddingValues: PaddingValues, viewModel: SettingViewModel) {

    val context = LocalContext.current
    val user by viewModel.user.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        SettingProfileComp(
            name = user?.name ?: "N/A",
            email = user?.email ?: "N/A",
            image = "https://www.shareicon.net/data/128x128/2016/09/15/829466_man_512x512.png"
        )
        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            contentColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column {
                SettingSwitchCard(
                    iconFilled = R.drawable.dark_mode_icon,
                    iconOutline = R.drawable.light_mode_icone,
                    title = "Dark Mode",
                    checked = true,
                    onCheckedChange = { isChecked ->

                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))
                SettingSwitchCard(
                    iconFilled = R.drawable.notification_filled_icon,
                    iconOutline = R.drawable.notification_icon,
                    title = "Notification",
                    checked = viewModel.notificationsEnabled.value,
                    onCheckedChange = { isChecked ->
                        viewModel.notificationsEnabled.value = isChecked
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))
            }

        }

        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            contentColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column {
                SettingCardComp(
                    icon = R.drawable.check_update,
                    title = "Check for update",
                    onClick = {
                        Toast.makeText(context, "App Is Updated", Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.feedback_icon,
                    title = "Rate",
                    onClick = {
                        /*TODO*/
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.privacy_icon,
                    title = "Privacy",
                    onClick = {
                        /*TODO*/
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                var userGuide by remember { mutableStateOf(false) }

                SettingCardComp(
                    icon = R.drawable.guide_user_icon,
                    title = "User Guide",
                    onClick = {
                        userGuide = true
                    }
                )
                if (userGuide) {
                    AlertDialogBox(
                        onDismissRequest = {
                            userGuide = false
                        },
                        onConfirmation = {
                            userGuide = false
                        },
                        dialogTitle = "User Guide",
                        dialogText = "We keep your last 3 years of expense, including this year. Older records are automatically removed.",
                        icon = R.drawable.guide_user_icon
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                var isFeedback by remember { mutableStateOf(false) }

                SettingCardComp(
                    icon = R.drawable.email_icon,
                    title = "Help & Feedback",
                    onClick = {
                        isFeedback = true
                    }
                )
                if (isFeedback) {
                    AlertDialogBox(
                        onDismissRequest = {
                            isFeedback = false
                        },
                        onConfirmation = {
                            isFeedback = false
                        },
                        dialogTitle = "Help & Feedback",
                        dialogText = "For feature requests, or feedback, please email us below. We're here to help!",
                        icon = R.drawable.email_icon,
                        email = "dipu@gmail.com"
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                var signOut by remember { mutableStateOf(false) }

                SettingCardComp(
                    icon = R.drawable.sign_out_icon,
                    title = "Sign Out",
                    onClick = {
                        signOut = true
                    },
                    textColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.error
                )
                if (signOut) {
                    AlertDialogBox(
                        onDismissRequest = {
                            signOut = false
                        },
                        onConfirmation = {
                         viewModel.onEvent(SettingUiEvent.SignOutClick)
                            signOut = false
                        },
                        dialogTitle = "Sign Out?",
                        dialogText = "You can safely sign out. Your data is securely saved in the cloud, ensuring it's available whenever you sign in.",
                        email = user?.email ?: "N/A",
                        icon = R.drawable.sign_out_icon,
                        confirmButtonColor = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        var isDeleteAccount by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.small,
                )
                .clickable {
                    isDeleteAccount = true
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .wrapContentWidth()
            ) {
                Text(
                    text = "Delete Account & Data",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onError
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
        if (isDeleteAccount) {
            AlertDialogBox(
                onDismissRequest = {
                    isDeleteAccount = false
                },
                onConfirmation = {
                    viewModel.onEvent(SettingUiEvent.DeleteAccountClick)
                    isDeleteAccount = false
                },
                dialogTitle = "Deleted Account?",
                dialogText = "Your account and data will be permanently deleted. You can create a new account anytime with the same email.",
                email = user?.email ?: "N/A",
                icon = R.drawable.delete_icon,
                confirmButtonColor = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
                )
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingProfileComp(
    name: String,
    email: String,
    image: Any? = null,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        contentColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlideImage(
                    model = image,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SettingCardComp(
    icon: Int,
    title: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = iconColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
        IconButton(onClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Go to $title",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingSwitchCard(
    iconFilled: Int,
    iconOutline: Int,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = if (checked) iconFilled else iconOutline),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}


@Composable
fun AlertDialogBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: Int,
    email: String? = null,
    confirmButtonColor: ButtonColors = ButtonDefaults.buttonColors(),
    dismissButtonColor: ButtonColors = ButtonDefaults.buttonColors(),
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon"
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                Text(
                    text = dialogText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                if (email != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.background,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                },
                shape = MaterialTheme.shapes.small,
                colors = confirmButtonColor,

                ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                },
                shape = MaterialTheme.shapes.small,
                colors = dismissButtonColor
            ) {
                Text("Dismiss")
            }
        }
    )
}