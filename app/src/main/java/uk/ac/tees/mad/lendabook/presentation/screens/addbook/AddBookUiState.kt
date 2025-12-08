package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import android.net.Uri

data class AddBookUiState(
    val localCoverUri: Uri? = null,    // For preview in UI
    val coverPhoto: String = "",       // Cloudinary URL
    val bookTitle: String = "",
    val authorName: String = "",
    val category: String = "Fiction",
    val condition: String = "New",
    val postalCode: String = "",
    val bookISBN: String = "",
)

