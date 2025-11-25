package uk.ac.tees.mad.lendabook.presentation.screens.bookDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.data.model.BookDoc
import uk.ac.tees.mad.lendabook.data.model.BookMetadata
import uk.ac.tees.mad.lendabook.domain.repo.ApiBookRepo
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val apiBookRepo: ApiBookRepo,
) : ViewModel() {

    private val _bookDoc = MutableStateFlow<BookMetadata?>(null)
    val bookDoc = _bookDoc.asStateFlow()

    fun getApiBookDetail(isbn: String) {
        viewModelScope.launch {
            val result = apiBookRepo.fetchBookByIsbn(isbn)
            result.onSuccess { metadata ->
                Log.d("TAG", "getApiBookDetail: $metadata")
                _bookDoc.value = metadata
            }.onFailure { e ->

            }
        }
    }
}