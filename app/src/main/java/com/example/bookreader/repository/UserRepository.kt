package com.example.bookreader.repository

import android.util.Log
import com.example.bookreader.data.db.UserDao
import com.example.bookreader.models.UserModel
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val goTrue: GoTrue,
    private val postgrest: Postgrest,
    private val userDao: UserDao
) {

    private val tag = "UserRepository"
    fun getCurrentUserAuth(): UserInfo? =
        goTrue.currentSessionOrNull()?.user

    suspend fun saveUser(user: UserModel) {
        saveUserRemote(user)
        saveUserLocally(user)
    }

    private suspend fun saveUserRemote(user: UserModel) {
        withContext(Dispatchers.IO) {
            try {
                postgrest["users"].insert(user)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
            }
        }
    }

    suspend fun saveUserLocally(user: UserModel) {
        withContext(Dispatchers.IO) {
            try {
                userDao.saveUser(user)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
            }
        }
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

    fun getUserByIdLocally() = userDao.getUsers()

    suspend fun isUserSaved(): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.isUserExists() > 0
        }
    }
}



