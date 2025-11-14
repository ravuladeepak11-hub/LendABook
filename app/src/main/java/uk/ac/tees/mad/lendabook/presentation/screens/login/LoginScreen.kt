package uk.ac.tees.mad.lendabook.presentation.screens.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.presentation.components.AppIcon
import uk.ac.tees.mad.lendabook.presentation.components.AppTitleText
import uk.ac.tees.mad.lendabook.presentation.components.textfields.EmailTextField
import uk.ac.tees.mad.lendabook.presentation.components.textfields.PasswordTextField
import uk.ac.tees.mad.lendabook.presentation.navigation.CreateAccountRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.HomeRoute
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.showToast

@Composable
fun LoginScreen(navHostController: NavHostController) {

    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    //For UiState
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).message
                context.showToast(errorMessage)
                viewModel.restUiState()
            }

            is UiState.Success -> {
                val successMessage = (uiState as UiState.Success).message
                context.showToast(successMessage)
                viewModel.restUiState()
            }

            else -> Unit
        }
    }

    //For Navigation
    LaunchedEffect(Unit) {
        viewModel.loginNavigation.collect { navigationEvent ->
            when (navigationEvent) {
                LoginNavigation.Forget -> {

                }

                LoginNavigation.Home -> {
                    navHostController.navigate(HomeRoute)
                }

                LoginNavigation.CreateAccount -> {
                    navHostController.navigate(CreateAccountRoute)
                }
            }
        }
    }

    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientBackground)),
    ) {
        LoginContent(viewModel, uiState)
    }

}

@Composable
fun LoginContent(viewModel: LoginViewModel, uiState: UiState) {

    val loginUiState by viewModel.loginUiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = Dimen.PaddingMedium),
        verticalArrangement = Arrangement.Center
    )
    {
        AppIcon(
            iconSize = 48.dp,
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerExtraLarge))
        Column() {
            AppTitleText(
                title = "Welcome Back!"
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
            EmailTextField(
                value = loginUiState.email,
                onValueChange = { newEmail ->
                    viewModel.onEvent(LoginUiEvent.EmailChanged(newEmail))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerSmall))
            PasswordTextField(
                value = loginUiState.password,
                onValueChange = { newPassword ->
                    viewModel.onEvent(LoginUiEvent.PasswordChange(newPassword))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
            Button(
                onClick = {
                    viewModel.onEvent(LoginUiEvent.LoginClicked)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = (uiState is UiState.Loading)
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = stringResource(R.string.login))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Don't have an account?")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(text = stringResource(R.string.create_account))
            }
            Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreviewContent() {
    if (LocalInspectionMode.current.not()) {
        return
    }

    var email by remember { mutableStateOf("user@example.com") }
    var password by remember { mutableStateOf("password123") }
    var isLoading by remember { mutableStateOf(false) }

    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientBackground)),
    ) {
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = Dimen.PaddingMedium),
            verticalArrangement = Arrangement.Center
        ) {
            AppIcon(
                iconSize = 48.dp,
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerExtraLarge))
            Column {
                AppTitleText(title = "Welcome Back!")
                Spacer(modifier = Modifier.height(Dimen.SpacerMedium))

                EmailTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimen.SpacerSmall))

                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )

                Spacer(modifier = Modifier.height(Dimen.SpacerMedium))

                Button(
                    onClick = { isLoading = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AnimatedContent(targetState = isLoading) { loading ->
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(text = stringResource(R.string.login))
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Don't have an account?")
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(text = stringResource(R.string.create_account))
                }
                Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
            }
        }
    }
}