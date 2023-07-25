package com.example.bookreader.models

data class DescriptionObject(val type: String, val value: String) {
    companion object {
        fun getBookDescription(description: Any?): String {
            return when (description) {
                is String -> {
                    return if (description.isNotEmpty())
                        description as String
                    else
                        "No description"
                }
                is DescriptionObject -> description.value
                else -> "No description"
            }
        }
    }
}