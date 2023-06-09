package com.example.bookreader.repository

import android.util.Log
import com.example.bookreader.models.UserModel
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val goTrue: GoTrue,
    private val postgrest: Postgrest
) {
    fun getCurrentUserAuth(): UserInfo? =
        goTrue.currentSessionOrNull()?.user

    suspend fun saveUser(user: UserModel) {
        saveUserRemote(user)
        saveUserLocally(user)
    }

    private suspend fun saveUserRemote(user: UserModel) {
        withContext(Dispatchers.IO) {
            postgrest["users"].insert(user)
        }
    }

    suspend fun saveUserLocally(user: UserModel) {
        // TODO: save user in room db
    }

    suspend fun fetchUserRemoteByUserId(userId: String): UserModel? =
        withContext(Dispatchers.IO) {
            try {
                val user = postgrest["users"].select() {
                    UserModel::userId eq userId
                }.decodeSingleOrNull<UserModel>()
                user
            } catch (e: Exception) {
                Log.e("repository", e.message.toString())
                throw Exception("No Connection")
            }
        }

    suspend fun invalidateSession() {
        goTrue.invalidateSession()
    }

    suspend fun getUserByIdLocally(userId: String): Boolean {
        // TODO:
        return false
    }
}



