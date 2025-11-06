package uk.ac.tees.mad.lendabook.presentation.screens.createAccount

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.presentation.components.AppTitleText
import uk.ac.tees.mad.lendabook.presentation.components.textfields.EmailTextField
import uk.ac.tees.mad.lendabook.presentation.components.textfields.NameTextField
import uk.ac.tees.mad.lendabook.presentation.components.textfields.PasswordTextField
import uk.ac.tees.mad.lendabook.presentation.navigation.ForgetRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.HomeRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.LoginRoute
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.showToast

@Composable
fun CreateAccountScreen(navController: NavHostController) {

    val viewModel: CreateAccountViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    //UiState
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

    //For Navigation
    LaunchedEffect(Unit) {
        viewModel.createAccountNavigation.collect { navigationEvent ->
            when (navigationEvent) {
                CreateAccountNavigation.Forget -> {
                    navController.navigate(ForgetRoute)
                }

                CreateAccountNavigation.Home -> {
                    navController.navigate(HomeRoute)
                }

                CreateAccountNavigation.Login -> {
                    navController.navigate(LoginRoute)
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
        CreateAccountContent(viewModel, uiState)
    }

}

@Composable
fun CreateAccountContent(viewModel: CreateAccountViewModel, uiState: UiState) {

    val createAccountUiState by viewModel.createAccountUiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = Dimen.PaddingMedium),
        verticalArrangement = Arrangement.Center
    ) {
        AppTitleText(
            title = "Join LendABook"
        )
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        Column {
            NameTextField(
                value = createAccountUiState.name,
                onValueChange = { newName ->
                    viewModel.onEvent(CreateAccountUiEvent.NameChanged(newName))
                },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    createAccountUiState.nameError?.let { error ->
                        Text(text = error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerSmall))

            EmailTextField(
                value = createAccountUiState.email,
                onValueChange = { newEmail ->
                    viewModel.onEvent(CreateAccountUiEvent.EmailChanged(newEmail))
                },
                supportingText = {
                    createAccountUiState.emailError?.let { error ->
                        Text(text = error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerSmall))

            PasswordTextField(
                value = createAccountUiState.password,
                onValueChange = { newPassword ->
                    viewModel.onEvent(CreateAccountUiEvent.PasswordChange(newPassword))
                },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    val errors = createAccountUiState.passwordError?.firstOrNull()
                    errors?.let {
                        Text(text = it)
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        Button(
            onClick = {
                viewModel.onEvent(CreateAccountUiEvent.CreateAccountClicked)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
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
                    Text(text = stringResource(R.string.create_account))
                }
            }
        }
    }
}