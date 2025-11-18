package uk.ac.tees.mad.lendabook.presentation.screens.browseBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.lendabook.domain.common.UiState
import uk.ac.tees.mad.lendabook.domain.model.BookDetail
import uk.ac.tees.mad.lendabook.domain.repo.AddBookRepo
import javax.inject.Inject

@HiltViewModel
class BrowseBookViewModel @Inject constructor(
    private val addBookRepo: AddBookRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _browseBookUiState = MutableStateFlow(BrowseBookUiState())
    val browseBookUiState = _browseBookUiState.asStateFlow()

    private val _bookList = MutableStateFlow<List<BookDetail>>(emptyList())
    val bookList = _bookList.asStateFlow()

    private val _browseBookNav = MutableSharedFlow<BrowseBookNavigation>()
    val browseBookNav = _browseBookNav.asSharedFlow()


    fun onEvent(event: BrowseBookUiEvent) {
        when (event) {
            is BrowseBookUiEvent.QueryChanged -> {
                _browseBookUiState.update {
                    it.copy(query =event.query)
                }
            }

            is BrowseBookUiEvent.FilterChanged -> {
                _browseBookUiState.update {
                    it.copy(filter =event.filter)
                }
            }

            BrowseBookUiEvent.AddBookClicked -> {
                viewModelScope.launch {
                    _browseBookNav.emit(BrowseBookNavigation.AddBook)
                }
            }

           is BrowseBookUiEvent.ViewBookDetailClicked -> {
                viewModelScope.launch {
                    _browseBookNav.emit(BrowseBookNavigation.BookDetails)
                }
            }
        }
    }

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            addBookRepo.getAllBooks().collect { bookDetails ->
                _bookList.value = bookDetails
            }
        }
    }
}