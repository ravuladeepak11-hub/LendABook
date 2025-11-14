package uk.ac.tees.mad.lendabook.presentation.screens.addbook

data class AddBookUiState(
    val coverPhoto: String = "",
    val bookTitle: String = "",
    val authorName: String = "",
    val category: String = "Fiction",
    val condition: String = "New",
    val postalCode: String = "",
    val bookISBN: String = "",
)
