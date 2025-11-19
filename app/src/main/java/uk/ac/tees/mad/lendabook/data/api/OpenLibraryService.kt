package uk.ac.tees.mad.lendabook.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.lendabook.data.model.OpenLibraryResponse

interface OpenLibraryService {
    @GET("search.json")
    suspend fun search(
        @Query("q") q: String? = null,
        @Query("author") author: String? = null,
        @Query("page") page: Int = 1,
    ): OpenLibraryResponse
}
