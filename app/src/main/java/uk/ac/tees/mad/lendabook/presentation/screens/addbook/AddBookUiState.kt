package uk.ac.tees.mad.lendabook.presentation.screens.addbook

data class AddBookUiState(
    val coverPhoto: String = "",
    val bookTitle: String = "",
    val authorName: String = "",
    val category: String = "",
    val condition: String = "",
    val postalCode: String = "",
    val bookISBN: String = "",
)
