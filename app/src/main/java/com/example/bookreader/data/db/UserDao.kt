package com.example.bookreader.data.db

import androidx.room.*
import com.example.bookreader.models.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserModel)

   @Query("SELECT COUNT() FROM users")
    suspend fun isUserExists(): Int

    @Delete
    suspend fun deleteUser(user: UserModel)
}