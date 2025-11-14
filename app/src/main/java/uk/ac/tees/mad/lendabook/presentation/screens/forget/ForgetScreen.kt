package uk.ac.tees.mad.lendabook.presentation.screens.forget

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
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.presentation.components.AppTitleText
import uk.ac.tees.mad.lendabook.presentation.components.textfields.EmailTextField
import uk.ac.tees.mad.lendabook.presentation.navigation.LoginRoute
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.showToast

@Composable
fun ForgetScreen(navController: NavHostController) {

    val viewModel: ForgetViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    //UiState
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                val successMessage = (uiState as UiState.Success).message
                context.showToast(successMessage)
                viewModel.restUiState()
            }

            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).message
                context.showToast(errorMessage)
                viewModel.restUiState()
            }

            else -> Unit
        }
    }

    //For Navigation
    LaunchedEffect(Unit) {
        viewModel.forgetNavigation.collect { navigationEvent ->
            when (navigationEvent) {
                ForgetNavigation.Login -> {
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
        ForgetContent(viewModel, uiState)
    }

}

@Composable
fun ForgetContent(viewModel: ForgetViewModel, uiState: UiState) {

    val focusManger = LocalFocusManager.current
    val forgetUiState by viewModel.forgetUiState.collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = Dimen.PaddingMedium),
        verticalArrangement = Arrangement.Center
    ) {
        Column {
            AppTitleText(
                title = stringResource(R.string.forget_password)
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
            EmailTextField(
                value = forgetUiState.email,
                onValueChange = { newEmail ->
                    viewModel.onEvent(ForgetUiEvent.EmailChanged(newEmail))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManger.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(Dimen.SpacerSmall))
            Button(
                onClick = {
                    viewModel.onEvent(ForgetUiEvent.SendRestClick)
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
                        Text(text = stringResource(R.string.reset_email))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ForgetScreenPreviewContent() {
    if (!LocalInspectionMode.current) return

    var email by remember { mutableStateOf("test@example.com") }
    var isLoading by remember { mutableStateOf(false) }

    val gradientBackground = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.background,
    )

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientBackground)),
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = Dimen.PaddingMedium),
            verticalArrangement = Arrangement.Center
        ) {

            AppTitleText(
                title = stringResource(R.string.forget_password)
            )

            Spacer(modifier = Modifier.height(Dimen.SpacerMedium))

            EmailTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(Dimen.SpacerSmall))

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
                        Text(text = stringResource(R.string.reset_email))
                    }
                }
            }
        }
    }
}