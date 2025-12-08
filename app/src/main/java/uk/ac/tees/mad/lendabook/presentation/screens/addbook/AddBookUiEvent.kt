package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import android.net.Uri

sealed class AddBookUiEvent {
    data class CoverChanged(val photoUri: String) : AddBookUiEvent()
    data class TitleChanged(val title: String) : AddBookUiEvent()
    data class AuthorChange(val author: String) : AddBookUiEvent()
    data class CategoryChanged(val category: String) : AddBookUiEvent()
    data class ConditionChanged(val condition: String) : AddBookUiEvent()
    data class PostCodeChanged(val postCode: String) : AddBookUiEvent()
    data class ISBNChanged(val isbn: String) : AddBookUiEvent()
    data class CoverImageChanged(val uri: Uri?) : AddBookUiEvent()
    object UploadBookClicked : AddBookUiEvent()

}


enum class AddBookNav{
    Dashboard
}