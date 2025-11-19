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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.R
import uk.ac.tees.mad.lendabook.data.model.BookDoc
import uk.ac.tees.mad.lendabook.domain.model.BookDetail
import uk.ac.tees.mad.lendabook.presentation.components.AppFilterChip
import uk.ac.tees.mad.lendabook.presentation.components.scaffold.DashboardScaffold
import uk.ac.tees.mad.lendabook.presentation.navigation.AddBookRoute
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
            SearchSection()
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
                        BookCard(
                            bookCover = book.coverUrl()
                                ?: "https://images.pexels.com/photos/46274/pexels-photo-46274.jpeg",
                            bookTitle = book.title ?: "Unknown Title",
                            bookAuthor = book.authorsAsString(),
                            condition = book.firstPublishYear?.toString() ?: "N/A",
                            onClickBook = {
                                viewModel.onEvent(
                                    BrowseBookUiEvent.ViewBookDetailClicked(book.isbns.toString())
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
fun SearchSection() {
    var query by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            maxLines = 1,
            placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
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
                fontWeight = FontWeight.Bold
            )
            Text(bookAuthor, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Browse Book Screen - LendABook")
@Composable
fun BrowseBookScreenPreview() {
    val sampleUserBooks = listOf(
        BookDetail(
            bookTitle = "The Great Gatsby",
            authorName = "F. Scott Fitzgerald",
            condition = "Like New",
            coverPhoto = "https://images.pexels.com/photos/46274/pexels-photo-46274.jpeg?w=800"
        ),
        BookDetail(
            bookTitle = "1984",
            authorName = "George Orwell",
            condition = "Good",
            coverPhoto = "https://images.pexels.com/photos/267885/pexels-photo-267885.jpeg?w=800"
        ),
        BookDetail(
            bookTitle = "To Kill a Mockingbird",
            authorName = "Harper Lee",
            condition = "Fair",
            coverPhoto = "https://images.pexels.com/photos/374722/pexels-photo-374722.jpeg?w=800"
        )
    )

    val sampleApiBooks = listOf(
        BookDoc(
            title = "Pride and Prejudice",
            firstPublishYear = 1813,
            isbns = listOf("9780141439518")
        ),
        BookDoc(
            title = "The Catcher in the Rye",
            firstPublishYear = 1951,
            isbns = listOf("9780316769488")
        )
    )

    DashboardScaffold(
        navController = rememberNavController(),
        topBar = {
            TopAppBar(
                title = { Text("LendABook") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SearchSection() }

            item {
                ConditionChips(
                    conditions = listOf("All", "Like New", "Good", "Fair", "Poor"),
                    selectedCondition = "All",
                    onClick = {}
                )
            }

            item {
                Text("User Uploaded Books", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(sampleUserBooks) { book ->
                        BookCard(
                            bookCover = book.coverPhoto,
                            bookTitle = book.bookTitle,
                            bookAuthor = book.authorName,
                            condition = book.condition,
                            onClickBook = {}
                        )
                    }
                }
            }

            item {
                Text("API Books", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(sampleApiBooks) { book ->
                        BookCard(
                            bookCover = book.coverUrl() ?: "https://images.pexels.com/photos/46274/pexels-photo-46274.jpeg",
                            bookTitle = book.title ?: "Unknown",
                            bookAuthor = book.authorsAsString(),
                            condition = book.firstPublishYear?.toString() ?: "N/A",
                            onClickBook = {}
                        )
                    }
                }
            }
        }
    }
}