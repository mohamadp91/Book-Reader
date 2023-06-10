package com.example.bookreader.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@Entity(tableName = "users")
data class UserModel(
    val id: Int = 1,

    @SerialName("user_id")
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,

    @SerialName("first_name")
    @ColumnInfo(name = "first_name")
    var firstName: String,

    @SerialName("last_name")
    @ColumnInfo(name = "last_name")
    var lastName: String,

    var bio: String?,
    val email: String
)