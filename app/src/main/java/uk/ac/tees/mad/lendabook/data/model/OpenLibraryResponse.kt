package uk.ac.tees.mad.lendabook.data.model

import com.google.gson.annotations.SerializedName

data class OpenLibraryResponse(
    @SerializedName("numFound") val numFound: Int = 0,
    @SerializedName("start") val start: Int = 0,
    @SerializedName("docs") val docs: List<BookDoc> = emptyList()
)

data class BookDoc(
    @SerializedName("title") val title: String? = null,
    @SerializedName("author_name") val authorName: List<String>? = null,
    @SerializedName("first_publish_year") val firstPublishYear: Int? = null,
    @SerializedName("cover_i") val coverId: Int? = null,
    @SerializedName("ia") val isbns: List<String>? = null,
    @SerializedName("key") val key: String? = null
) {
    fun coverUrl(size: String = "M"): String? {
        return coverId?.let { "https://covers.openlibrary.org/b/id/$it-$size.jpg" }
    }
    fun authorsAsString(): String = authorName?.joinToString(", ") ?: "Unknown author"
}