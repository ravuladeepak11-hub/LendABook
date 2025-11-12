package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.domain.model.BookDetail
import uk.ac.tees.mad.lendabook.domain.repo.AddBookRepo
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookRepo: AddBookRepo,
    private val authRepo: FirebaseAuthRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _addBookUiState = MutableStateFlow(AddBookUiState())
    val addBookUiState = _addBookUiState.asStateFlow()

    fun onEvent(event: AddBookUiEvent) {
        when (event) {
            is AddBookUiEvent.CoverChanged -> TODO()
            is AddBookUiEvent.TitleChanged -> {
                _addBookUiState.update {
                    it.copy(
                        bookTitle = event.title
                    )
                }
            }

            is AddBookUiEvent.AuthorChange -> {
                _addBookUiState.update {
                    it.copy(
                        authorName = event.author
                    )
                }
            }

            is AddBookUiEvent.CategoryChanged -> {
                _addBookUiState.update {
                    it.copy(
                        category = event.category
                    )
                }
            }

            is AddBookUiEvent.ConditionChanged -> {
                _addBookUiState.update {
                    it.copy(
                        condition = event.condition
                    )
                }
            }

            is AddBookUiEvent.ISBNChanged -> {
                _addBookUiState.update {
                    it.copy(
                        bookISBN = event.isbn
                    )
                }
            }

            is AddBookUiEvent.PostCodeChanged -> {
                _addBookUiState.update {
                    it.copy(
                        postalCode = event.postCode
                    )
                }
            }

            AddBookUiEvent.UploadBookClicked -> {
                saveBookDetail()
            }
        }
    }

    private fun saveBookDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val userId = authRepo.getUserId() ?: ""
            addBookRepo.saveBook(
                bookDetail = BookDetail(
                    userId = userId,
                    coverPhoto = addBookUiState.value.coverPhoto,
                    bookTitle = addBookUiState.value.bookTitle,
                    authorName = addBookUiState.value.authorName,
                    category = addBookUiState.value.category,
                    condition = addBookUiState.value.condition,
                    postalCode = addBookUiState.value.postalCode,
                    bookISBN = addBookUiState.value.bookISBN
                )
            ).onSuccess {
                _uiState.value = UiState.Success("Saved Book Successfully!")
            }.onFailure {
                _uiState.value = UiState.Error(it.localizedMessage ?: "Upload Book Failed!")
            }
        }
    }


    fun restUiState() {
        _uiState.value = UiState.Idle
    }


}