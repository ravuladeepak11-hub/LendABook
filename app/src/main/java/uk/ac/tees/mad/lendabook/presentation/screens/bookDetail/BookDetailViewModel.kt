package uk.ac.tees.mad.lendabook.presentation.screens.bookDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.lendabook.data.model.Author
import uk.ac.tees.mad.lendabook.data.model.BookDoc
import uk.ac.tees.mad.lendabook.data.model.BookMetadata
import uk.ac.tees.mad.lendabook.data.model.CoverInfo
import uk.ac.tees.mad.lendabook.data.model.Identifiers
import uk.ac.tees.mad.lendabook.data.model.Publisher
import uk.ac.tees.mad.lendabook.domain.model.BookDetail
import uk.ac.tees.mad.lendabook.domain.repo.ApiBookRepo
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val apiBookRepo: ApiBookRepo,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _bookDoc = MutableStateFlow<BookMetadata?>(null)
    val bookDoc = _bookDoc.asStateFlow()

    fun getApiBookDetail(isbn: String) {
        viewModelScope.launch {
            val result = apiBookRepo.fetchBookByIsbn(isbn)

            result.onSuccess { metadata ->
                if (metadata != null) {
                    _bookDoc.value = metadata
                    return@onSuccess
                }

                // API returned null → fallback to Firestore
                try {
                    val doc = firestore.collection("books")
                        .document(isbn)
                        .get()
                        .await()

                    if (!doc.exists()) {
                        Log.e("BookDetail", "Firestore doc not found")
                        return@onSuccess
                    }

                    val fireData = doc.toObject(BookDetail::class.java)
                    if (fireData == null) {
                        Log.e("BookDetail", "Failed to parse Firestore document")
                        return@onSuccess
                    }

                    // --- Build the BookMetadata object correctly ---
                    val mapped = BookMetadata(
                        title = fireData.bookTitle,
                        authors = listOf(
                            Author(
                                name = fireData.authorName,
                                url = null
                            )
                        ),
                        publishDate = null,
                        numberOfPages = null,

                        cover = CoverInfo(
                            small = fireData.coverPhoto,
                            medium = fireData.coverPhoto,
                            large = fireData.coverPhoto
                        ),

                        identifiers = Identifiers(
                            isbn_10 = listOf(isbn),
                            isbn_13 = null,
                            lccn = null,
                            oclc = null
                        ),

                        publishers = listOf(
                            Publisher(
                                name = fireData.userId
                            )
                        )
                    )

                    _bookDoc.value = mapped

                } catch (e: Exception) {
                    Log.e("BookDetail", "Firestore error", e)
                }
            }.onFailure {
                Log.e("BookDetail", "API failure")
            }
        }
    }
}