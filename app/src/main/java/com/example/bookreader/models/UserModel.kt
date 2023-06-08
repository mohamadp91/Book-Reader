package com.example.bookreader.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserModel(
    val id: Int = 1,
    @SerialName("user_id")
    val userId: String,
    @SerialName("first_name")
    var firstName: String,
    @SerialName("last_name")
    var lastName: String,
    var bio: String?,
    val email: String
)