package com.example.bookreader.models

data class DocsApiModel(
    val docs: List<Doc>,
    val numFound: Int,
    val numFoundExact: Boolean,
    val num_found: Int,
    val offset: Any,
    val q: String,
    val start: Int
)