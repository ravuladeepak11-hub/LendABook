package uk.ac.tees.mad.lendabook.presentation.components.atomic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppIcon(
    iconSize: Dp,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Icon(
            imageVector = Icons.Default.Book,
            contentDescription = "Book Logo",
            modifier = Modifier.size(iconSize),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}