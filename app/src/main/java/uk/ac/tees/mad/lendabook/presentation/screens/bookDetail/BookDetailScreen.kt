package uk.ac.tees.mad.lendabook.presentation.screens.bookDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.tees.mad.lendabook.presentation.navigation.DashboardRoute
import uk.ac.tees.mad.lendabook.utils.Dimen
import uk.ac.tees.mad.lendabook.utils.buildCoverUrl


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(navController: NavHostController) {

    val viewModel: BookDetailViewModel = hiltViewModel()

    LaunchedEffect(Unit) {

        viewModel.getApiBookDetail(isbn = "isbn")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Detail") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(DashboardRoute)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        BookDetailContent(paddingValues, viewModel)
    }
}

@Composable
fun BookDetailContent(paddingValues: PaddingValues, viewModel: BookDetailViewModel) {

    val bookDoc by viewModel.bookDoc.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = Dimen.SpacerSmall)
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookCoverImage(imageUrl = bookDoc?.cover?.large)
        Spacer(Modifier.height(20.dp))
        BookInfoSection(
            title = bookDoc?.title ?: "N/a",
            author = "Matt Haig",
            tags = listOf("Available", "Fantasy", "Like New"),
            isbn = "978-0735211292",
            published = bookDoc?.publishDate ?: "N/A"
        )
    }
}


@Composable
fun BookInfoSection(
    title: String,
    author: String,
    tags: List<String>,
    isbn: String,
    published: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "by $author",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach {
                AssistChip(
                    onClick = {},
                    label = { Text(it) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("ISBN: $isbn", style = MaterialTheme.typography.labelLarge)
        if (published.isNotEmpty()) {
            Text("Published: $published", style = MaterialTheme.typography.labelLarge)
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookCoverImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    GlideImage(
        model = imageUrl ?: "https://images.pexels.com/photos/46274/pexels-photo-46274.jpeg",
        contentDescription = "Book cover",
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(Dimen.RadiusMedium)),
        contentScale = ContentScale.Crop
    )
}