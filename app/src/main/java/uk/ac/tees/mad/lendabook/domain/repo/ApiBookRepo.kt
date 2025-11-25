package uk.ac.tees.mad.lendabook.domain.repo

import uk.ac.tees.mad.lendabook.data.model.BookDoc
import uk.ac.tees.mad.lendabook.data.model.BookMetadata

interface ApiBookRepo {
    suspend fun search(query: String, searchByAuthor: Boolean): Result<List<BookDoc>>

    suspend fun fetchBookByIsbn(isbn: String): Result<BookMetadata?>
}