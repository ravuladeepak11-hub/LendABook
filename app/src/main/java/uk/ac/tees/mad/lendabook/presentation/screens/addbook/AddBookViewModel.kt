package uk.ac.tees.mad.lendabook.presentation.screens.addbook

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
import uk.ac.tees.mad.lendabook.domain.repo.FirebaseAuthRepo
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookRepo: AddBookRepo,
    private val authRepo: FirebaseAuthRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _addBookUiState = MutableStateFlow(AddBookUiState())
    val addBookUiState = _addBookUiState.asStateFlow()

    private val _addBookNav = MutableSharedFlow<AddBookNav>()
    val addBookNav = _addBookNav.asSharedFlow()

    // Use unsigned preset only – NEVER api_secret
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dx7fjoldk",
            "api_key" to "397748142736691",
            "api_secret" to "swyBwNbJ-SG9BctFbA_WQ5aGOpU"
        )
    )

    fun onEvent(event: AddBookUiEvent) {
        when (event) {

            is AddBookUiEvent.CoverImageChanged -> {
                _addBookUiState.update { it.copy(localCoverUri = event.uri) }

                if (event.uri == null) {
                    _addBookUiState.update { it.copy(coverPhoto = "") }
                    return
                }

                uploadCoverToCloudinary(event.uri)
            }

            is AddBookUiEvent.TitleChanged -> {
                _addBookUiState.update { it.copy(bookTitle = event.title) }
            }

            is AddBookUiEvent.AuthorChange -> {
                _addBookUiState.update { it.copy(authorName = event.author) }
            }

            is AddBookUiEvent.CategoryChanged -> {
                _addBookUiState.update { it.copy(category = event.category) }
            }

            is AddBookUiEvent.ConditionChanged -> {
                _addBookUiState.update { it.copy(condition = event.condition) }
            }

            is AddBookUiEvent.ISBNChanged -> {
                _addBookUiState.update { it.copy(bookISBN = event.isbn) }
            }

            is AddBookUiEvent.PostCodeChanged -> {
                _addBookUiState.update { it.copy(postalCode = event.postCode) }
            }

            AddBookUiEvent.UploadBookClicked -> saveBookDetail()
            else -> {}
        }
    }

    private fun uploadCoverToCloudinary(uri: Uri) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _uiState.value = UiState.Loading

                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: throw IllegalStateException("Cannot open image stream")

                val bytes = inputStream.readBytes()

                val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
                tempFile.writeBytes(bytes)

                val uploadResult = cloudinary.uploader().upload(
                    tempFile,
                    mapOf("upload_preset" to "unsigned_book_upload")
                )

                val url = uploadResult["secure_url"] as? String
                    ?: throw IllegalStateException("Cloudinary did not provide URL")

                _addBookUiState.update { it.copy(coverPhoto = url) }
                _uiState.value = UiState.Idle

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error("Upload failed: ${e.localizedMessage ?: "unknown error"}")            }
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
                Log.d("Firestore", addBookUiState.value.bookTitle)
                _uiState.value = UiState.Success("Saved Book Successfully!")
                _addBookNav.emit(AddBookNav.Dashboard)

            }.onFailure {
                _uiState.value = UiState.Error(it.localizedMessage ?: "Upload Book Failed!")
            }
        }
    }

    fun restUiState() {
        _uiState.value = UiState.Idle
    }
}
