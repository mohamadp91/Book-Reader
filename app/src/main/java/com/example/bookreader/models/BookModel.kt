package com.example.bookreader.models

data class BookModel(
    val id: String? = null,
    val title: String? = null,
    val authors: List<String?> = emptyList(),
    val notes: String? = null,
    val rate: Float? = null,
)
