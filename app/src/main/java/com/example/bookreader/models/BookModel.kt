package com.example.bookreader.models

import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class BookModel(
    val id: Int = 1,
    @SerialName(value = "book_id")
    val bookId: String? = null,
    val title: String? = null,
    val authors: List<String>? = null,
    val categories: List<String>? = null,
    val notes: String? = null,
    val description: String? = null,
    @SerialName(value = "published_date")
    val publishedDate: String? = null,
    @SerialName(value = "page_count")
    val pageCount: Int? = null,
    @SerialName(value = "image_url")
    val imageUrl: String? = null,
    val rate: Double? = null,
    @SerialName(value = "user_id")
    val userId: String,
    @SerialName(value = "start_reading")
    val startReading: LocalDate? = null,
    @SerialName(value = "end_reading")
    val endReading: LocalDate? = null
)
