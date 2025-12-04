package uk.ac.tees.mad.lendabook.presentation.screens.browseBook

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.domain.model.BookDetail
import uk.ac.tees.mad.lendabook.presentation.components.AppFilterChip
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.AddBookRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.BookDetailRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.BrowseBookRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.ForgetRoute
import uk.ac.tees.mad.lendabook.presentation.navigation.LoginRoute
import uk.ac.tees.mad.lendabook.presentation.screens.browseBook.SearchSection
import uk.ac.tees.mad.lendabook.presentation.screens.createAccount.CreateAccountNavigation
import uk.ac.tees.mad.lendabook.utils.Dimen

fun NavGraphBuilder.browseBookRoute(navController: NavHostController) =
    composable<BrowseBookRoute>() {
        BrowseBookScreen(navController = navController)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseBookScreen(navController: NavHostController) {

    val viewModel: BrowseBookViewModel = hiltViewModel()

    //For Navigation
    LaunchedEffect(Unit) {
        viewModel.browseBookNav.collect { navigationEvent ->
            when (navigationEvent) {
                BrowseBookNavigation.AddBook -> {
                    navController.navigate(AddBookRoute)
                }

                BrowseBookNavigation.BookDetails -> {
                    navController.navigate(BookDetailRoute)
                }
            }
        }
    }

    DashboardScaffold(navController = navController, topBar = {
        TopAppBar(
            title = { Text(stringResource(id = R.string.lendabook)) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                viewModel.onEvent(BrowseBookUiEvent.AddBookClicked)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_book))
        }
    }
    ) { paddingValues ->
        BrowseBookContent(paddingValues, viewModel)
    }
}


@Composable
fun BrowseBookContent(paddingValues: PaddingValues, viewModel: BrowseBookViewModel) {
    val bookList by viewModel.bookList.collectAsState()
    val apiBookDocs by viewModel.bookDocList.collectAsState()
    val conditionList = stringArrayResource(id = R.array.book_conditions).toList()
    val browseBookUiState by viewModel.browseBookUiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(Dimen.SpacerMedium)
    ) {
        // Search section
        item {
            SearchSection(
                query = browseBookUiState.query,
                onQueryChanged = {
                    viewModel.onEvent(BrowseBookUiEvent.QueryChanged(it))
                }
            )
        }

        // API Books Section
        if (apiBookDocs.isNotEmpty()) {
            item {
                Text(
                    text = "API Books",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(Dimen.SpacerSmall))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimen.SpacerSmall)
                ) {
                    items(apiBookDocs) { book ->
                        Log.d("TAG", "BrowseBookContent: ${book.isbns}")
                        BookCard(
                            bookCover = book.coverUrl() ?: "https://images.pexels.com/photos/46274/pexels-photo-46274.jpeg",
                            bookTitle = book.title ?: "Unknown Title",
                            bookAuthor = book.authorsAsString(),
                            condition = book.firstPublishYear?.toString() ?: "N/A",
                            onClickBook = {
                                book.isbns?.map { isbn ->
                                    viewModel.onEvent(
                                        BrowseBookUiEvent.ViewBookDetailClicked(isbn)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        // Condition chips
        item {
            ConditionChips(
                conditions = conditionList,
                selectedCondition = browseBookUiState.filter,
                onClick = { viewModel.onEvent(BrowseBookUiEvent.FilterChanged(it)) }
            )
        }

        // User Uploaded Books section
        if (bookList.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.user_upload_books),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(Dimen.SpacerSmall))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimen.SpacerSmall)
                ) {
                    items(bookList) { book ->
                        BookCard(
                            bookCover = book.coverPhoto,
                            bookTitle = book.bookTitle,
                            bookAuthor = book.authorName,
                            condition = book.condition,
                            onClickBook = {
                                viewModel.onEvent(
                                    BrowseBookUiEvent.ViewBookDetailClicked(book.bookISBN)
                                )
                            }
                        )
                    }
                }
            }
        }


    }
}

@Composable
fun SearchSection(
    query: String,
    onQueryChanged: (query: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { onQueryChanged(it) },
            maxLines = 1,
            placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

    }
}

@Composable
fun ConditionChips(
    modifier: Modifier = Modifier,
    conditions: List<String>,
    selectedCondition: String,
    onClick: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(conditions) { condition ->
            AppFilterChip(
                label = condition,
                selected = condition == selectedCondition,
                onClick = {
                    onClick(condition)
                }
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookCard(
    bookCover: String,
    bookTitle: String,
    bookAuthor: String,
    condition: String,
    onClickBook: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable {
                onClickBook()
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Box {
                GlideImage(
                    model = bookCover,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(MaterialTheme.shapes.small)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color(0xFF27C29D),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        condition, color = Color.White, style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(Modifier.height(Dimen.SpacerSmall))
            Text(
                bookTitle,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                bookAuthor,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
