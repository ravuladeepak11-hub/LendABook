package uk.ac.tees.mad.lendabook.data.model

import com.google.gson.annotations.SerializedName

data class BookMetadata(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<Author>?,
    @SerializedName("publish_date") val publishDate: String?,
    @SerializedName("number_of_pages") val numberOfPages: Int?,
    @SerializedName("cover") val cover: CoverInfo?,
    @SerializedName("identifiers") val identifiers: Identifiers?,
    @SerializedName("publishers") val publishers: List<Publisher>?
)

data class Author(
    val name: String?,
    val url: String?
)

data class CoverInfo(
    val small: String?,
    val medium: String?,
    val large: String?
)

data class Publisher(
    val name: String?
)

data class Identifiers(
    val isbn_10: List<String>?,
    val isbn_13: List<String>?,
    val lccn: List<String>?,
    val oclc: List<String>?
)