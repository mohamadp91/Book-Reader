package com.example.bookreader.models


data class BookApiModel(
    val title: String,
    val authors: List<Any>,
    val created: Created,
    val description: Any,
    val key: String,
    val subjects: List<String>,
    val covers: List<Int>? = null
)