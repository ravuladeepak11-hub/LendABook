package uk.ac.tees.mad.lendabook.domain.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.domain.model.BookDetail

interface AddBookRepo {
    suspend fun saveBook(bookDetail: BookDetail): Result<Unit>
    fun getAllBooks(): Flow<List<BookDetail>>
    suspend fun getBook(userISBN: String): Result<BookDetail>
}