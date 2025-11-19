package uk.ac.tees.mad.lendabook.domain.repo

import uk.ac.tees.mad.lendabook.data.model.BookDoc

interface ApiBookRepo {
    suspend fun search(query: String, searchByAuthor: Boolean): Result<List<BookDoc>>
}