package uk.ac.tees.mad.lendabook.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.lendabook.data.api.OpenLibraryService
import uk.ac.tees.mad.lendabook.data.model.BookDoc
import uk.ac.tees.mad.lendabook.domain.repo.ApiBookRepo
import javax.inject.Inject

class ApiBookRepoImpl @Inject constructor(
    private val service: OpenLibraryService,
) : ApiBookRepo {

    override suspend fun search(
        query: String,
        searchByAuthor: Boolean,
    ): Result<List<BookDoc>> {
        if (query.isBlank()) return Result.success(emptyList())
        return try {
            withContext(Dispatchers.IO) {
                val response = if (searchByAuthor) {
                    service.search(author = query)
                } else {
                    service.search(q = query)
                }
                Result.success(response.docs)
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}