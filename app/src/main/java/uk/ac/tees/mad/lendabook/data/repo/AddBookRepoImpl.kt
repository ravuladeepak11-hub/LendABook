package uk.ac.tees.mad.lendabook.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.lendabook.domain.model.BookDetail
import uk.ac.tees.mad.lendabook.domain.repo.AddBookRepo
import javax.inject.Inject


class AddBookRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AddBookRepo {

    companion object {
        private const val BOOK_COLLECTION = "books"
    }

    override suspend fun saveBook(bookDetail: BookDetail): Result<Unit> {
        return try {
            firestore.collection(BOOK_COLLECTION)
                .document(bookDetail.bookISBN)
                .set(bookDetail)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override fun getAllBooks(): Flow<List<BookDetail>> = flow {
        try {
            val snapshot = firestore.collection(BOOK_COLLECTION)
                .get()
                .await()

            val books = snapshot.documents.mapNotNull { document ->
                document.toObject(BookDetail::class.java)
            }
            emit(books)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }


    override suspend fun getBook(userISBN: String): Result<BookDetail> {
        return try {
            val documentSnapshot = firestore.collection(BOOK_COLLECTION)
                .document(userISBN)
                .get()
                .await()

            val book = documentSnapshot.toObject(BookDetail::class.java)
            if (book != null) {
                Result.success(book)
            } else {
                Result.failure(Exception("Book not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}