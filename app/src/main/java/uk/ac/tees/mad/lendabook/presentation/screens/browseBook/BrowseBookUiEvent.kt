package uk.ac.tees.mad.lendabook.presentation.screens.browseBook

sealed class BrowseBookUiEvent {
    object AddBookClicked : BrowseBookUiEvent()
    data class ViewBookDetailClicked(val isbn: String) : BrowseBookUiEvent()
    data class QueryChanged(val query: String) : BrowseBookUiEvent()
    data class FilterChanged(val filter: String) : BrowseBookUiEvent()
}

enum class BrowseBookNavigation {
    AddBook, BookDetails
}