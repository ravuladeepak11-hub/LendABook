package uk.ac.tees.mad.lendabook.domain.model

data class BookDetail(
    val userId: String = "",
    val coverPhoto: String = "",
    val bookTitle: String = "",
    val authorName: String = "",
    val category: String = "",
    val condition: String = "",
    val postalCode: String = "",
    val bookISBN: String = ""
)

