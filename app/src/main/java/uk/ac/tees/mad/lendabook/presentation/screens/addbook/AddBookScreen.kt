package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.presentation.navigation.DashboardRoute
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.showToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(navController: NavHostController) {

    val viewModel: AddBookViewModel = hiltViewModel()
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

    //Navigation
    LaunchedEffect(Unit) {
        viewModel.addBookNav.collect { nav ->
            when (nav) {
                AddBookNav.Dashboard -> {
                    navController.navigate(DashboardRoute)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.add_new_book)) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
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
fun AddBookContent(
    paddingValues: PaddingValues,
    viewModel: AddBookViewModel,
    uiState: UiState,
) {

    val addBookUiState by viewModel.addBookUiState.collectAsState()
    val categories = stringArrayResource(id = R.array.book_categories)
    val conditions = stringArrayResource(id = R.array.book_conditions)

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
            Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = stringResource(id = R.string.scan_isbn)
            )
            Spacer(Modifier.width(Dimen.SpacerSmall))
            Text(stringResource(id = R.string.scan_isbn))
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
                text = stringResource(id = R.string.book_cover),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.height(Dimen.SpacerSmall))
            Text(
                text = stringResource(id = R.string.add_cover_image_prompt),
                color = Color.Gray
            )
            Spacer(Modifier.height(Dimen.SpacerMedium))
            OutlinedButton(
                onClick = { /* Capture cover */ },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = stringResource(id = R.string.capture_cover)
                )
                Spacer(Modifier.width(6.dp))
                Text(stringResource(id = R.string.capture_cover))
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
                label = { Text(stringResource(id = R.string.enter_book_title)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = addBookUiState.authorName,
                onValueChange = { viewModel.onEvent(AddBookUiEvent.AuthorChange(it)) },
                label = { Text(stringResource(id = R.string.enter_author_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            DropdownSelector(
                label = stringResource(id = R.string.category),
                selectedValue = addBookUiState.category,
                options = categories.toList()
            ) {
                viewModel.onEvent(
                    AddBookUiEvent.CategoryChanged(it)
                )
            }

            DropdownSelector(
                label = stringResource(id = R.string.condition),
                selectedValue = addBookUiState.condition,
                options = conditions.toList()
            ) {
                viewModel.onEvent(
                    AddBookUiEvent.ConditionChanged(it)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(Dimen.SpacerSmall)) {
                OutlinedTextField(
                    value = addBookUiState.postalCode,
                    onValueChange = { viewModel.onEvent(AddBookUiEvent.PostCodeChanged(it)) },
                    label = { Text(stringResource(id = R.string.postcode)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = addBookUiState.bookISBN,
                    onValueChange = { viewModel.onEvent(AddBookUiEvent.ISBNChanged(it)) },
                    label = {
                        Text(
                            stringResource(id = R.string.isbn),
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
                    Text(text = stringResource(R.string.upload_book))
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "LendABook – Add Book Screen")
@Composable
fun AddBookScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Book") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Scan ISBN Button
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Scan ISBN")
            }

            // Book Cover Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Book Cover", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text("Add a clear photo of your book cover", color = Color.Gray)
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {},
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Capture Cover")
                }
            }

            // Book Details
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = "The Midnight Library",
                    onValueChange = {},
                    label = { Text("Book Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = "Matt Haig",
                    onValueChange = {},
                    label = { Text("Author Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Category Dropdown
                var categoryExpanded by remember { mutableStateOf(false) }
                var selectedCategory by remember { mutableStateOf("Fiction") }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        label = { Text("Category") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoryExpanded = true }
                    )
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        listOf("Fiction", "Non-Fiction", "Science", "Biography", "Fantasy").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedCategory = it
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Condition Dropdown
                var conditionExpanded by remember { mutableStateOf(false) }
                var selectedCondition by remember { mutableStateOf("Like New") }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCondition,
                        onValueChange = {},
                        label = { Text("Condition") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { conditionExpanded = true }
                    )
                    DropdownMenu(
                        expanded = conditionExpanded,
                        onDismissRequest = { conditionExpanded = false }
                    ) {
                        listOf("Like New", "Very Good", "Good", "Fair", "Poor").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedCondition = it
                                    conditionExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = "TS1 3BX",
                        onValueChange = {},
                        label = { Text("Postcode") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = "978-0525559474",
                        onValueChange = {},
                        label = { Text("ISBN") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Upload Button
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Upload Book", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}




