package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.presentation.navigation.DashboardRoute
import uk.ac.tees.mad.lendabook.presentation.screens.setting.SettingViewModel
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.NotificationHelper
import uk.ac.tees.mad.lendabook.utils.showToast
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(navController: NavHostController) {

    val viewModel: AddBookViewModel = hiltViewModel()
    val settingVM: SettingViewModel = hiltViewModel()
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
                    IconButton(onClick = { navController.navigateUp() }) {
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
        AddBookContent(paddingValues, viewModel, uiState, settingVM)
    }
}

@Composable
fun AddBookContent(
    paddingValues: PaddingValues,
    viewModel: AddBookViewModel,
    uiState: UiState,
    settingVM: SettingViewModel,
) {
    val settingUiState by settingVM.settingUiState.collectAsState()
    val addBookUiState by viewModel.addBookUiState.collectAsState()
    val categories = stringArrayResource(id = R.array.book_categories)
    val conditions = stringArrayResource(id = R.array.book_conditions)
    val context = LocalContext.current

    // State for camera capture
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Create a temporary file for the camera
    val photoFile = remember {
        File(context.cacheDir, "book_cover_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
        }
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = photoUri
            viewModel.onEvent(AddBookUiEvent.CoverImageChanged(photoUri))
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            viewModel.onEvent(AddBookUiEvent.CoverImageChanged(it))
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            cameraLauncher.launch(photoUri)
        } else {
            context.showToast("Camera permission is required to take photos")
        }
    }

    // Image source dialog
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Select Image Source") },
            text = { Text("Choose where to get the book cover image from") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Gallery")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = Dimen.PaddingMedium)
            .verticalScroll(rememberScrollState())
    ) {


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

            // Show image if captured, otherwise show prompt
            if (imageUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Book Cover",
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Delete button overlay
                    IconButton(
                        onClick = {
                            imageUri = null
                            viewModel.onEvent(AddBookUiEvent.CoverImageChanged(null))
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Remove image",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(Modifier.height(Dimen.SpacerMedium))
                OutlinedButton(
                    onClick = { showImageSourceDialog = true },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = stringResource(id = R.string.capture_cover)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Change Image")
                }
            } else {
                Text(
                    text = stringResource(id = R.string.add_cover_image_prompt),
                    color = Color.Gray
                )
                Spacer(Modifier.height(Dimen.SpacerMedium))
                OutlinedButton(
                    onClick = { showImageSourceDialog = true },
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
                if (settingUiState.settings.notificationsEnabled) {
                    NotificationHelper.showBookNotification(
                        context,
                        addBookUiState.bookTitle,
                        addBookUiState.authorName
                    )
                }
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