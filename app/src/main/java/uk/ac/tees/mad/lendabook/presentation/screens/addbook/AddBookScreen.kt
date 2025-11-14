package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.showToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(viewModel: AddBookViewModel = hiltViewModel()) {


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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add a New Book") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        AddBookContent(paddingValues, viewModel, uiState)
    }
}

@Composable
fun AddBookContent(paddingValues: PaddingValues, viewModel: AddBookViewModel, uiState: UiState) {

    val addBookUiState by viewModel.addBookUiState.collectAsState()
    val categories = listOf("Fiction", "Non-fiction", "Comics", "Biography")
    val conditions = listOf("New", "Used", "Good", "Fair")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = Dimen.PaddingMedium)
            .verticalScroll(rememberScrollState())
    ) {

        //Scanner Button
        Button(
            onClick = {
                /* Handle scan */
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan ISBN")
            Spacer(Modifier.width(Dimen.SpacerSmall))
            Text("Scan ISBN")
        }

        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        //BookCoverCaptureSection
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(Dimen.RadiusSmall))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Book Cover",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.height(Dimen.SpacerSmall))
            Text(
                text = "Tap the button below to add a cover image.",
                color = Color.Gray
            )
            Spacer(Modifier.height(Dimen.SpacerMedium))
            OutlinedButton(
                onClick = { /* Capture cover */ },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Capture Cover")
                Spacer(Modifier.width(6.dp))
                Text("Capture Cover")
            }
        }
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        //Book Details Section
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.SpacerSmall)
        ) {
            OutlinedTextField(
                value = addBookUiState.bookTitle,
                onValueChange = {
                    viewModel.onEvent(AddBookUiEvent.TitleChanged(it))
                },
                label = { Text("Enter book title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = addBookUiState.authorName,
                onValueChange = { viewModel.onEvent(AddBookUiEvent.AuthorChange(it)) },
                label = { Text("Enter author's name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            DropdownSelector(
                label = "Category",
                selectedValue = addBookUiState.category,
                options = categories
            ) {
                viewModel.onEvent(
                    AddBookUiEvent.CategoryChanged(it)
                )
            }

            DropdownSelector(
                label = "Condition",
                selectedValue = addBookUiState.condition,
                options = conditions
            ) {
                viewModel.onEvent(
                    AddBookUiEvent.ConditionChanged(it)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(Dimen.SpacerSmall)) {
                OutlinedTextField(
                    value = addBookUiState.postalCode,
                    onValueChange = { viewModel.onEvent(AddBookUiEvent.PostCodeChanged(it)) },
                    label = { Text("Postcode") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = addBookUiState.bookISBN,
                    onValueChange = { viewModel.onEvent(AddBookUiEvent.ISBNChanged(it)) },
                    label = {
                        Text(
                            "ISBN",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimen.SpacerMedium))
        //Upload Book Button
        Button(
            onClick = {
                viewModel.onEvent(AddBookUiEvent.UploadBookClicked)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
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


@Composable
fun DropdownSelector(
    label: String,
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun pre(){
    val viewModel = hiltViewModel<AddBookViewModel>()
    AddBookScreen(viewModel)
}

