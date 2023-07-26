package com.example.bookreader.models


data class BookApiModel(
    var id: String,
    val title: String,
    val created: Created,
    val description: Any,
    val key: String,
    val subjects: List<String>,
    val covers: List<Int>? = null,
    var bookAuthors: String,
    var pageCount: String,
)