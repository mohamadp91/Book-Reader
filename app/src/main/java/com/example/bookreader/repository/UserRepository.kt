package com.example.bookreader.repository

import com.example.bookreader.models.UserModel
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
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

    private suspend fun saveUserRemote(user: UserModel) = postgrest["users"].insert(user)

    suspend fun saveUserLocally(user: UserModel) {
        // TODO: save user in room db
    }

    suspend fun fetchUserRemoteByUserId(userId: String): UserModel? =
        postgrest["users"].select() {
            UserModel::userId eq userId
        }.decodeSingleOrNull<UserModel>()

    fun invalidateSession() {
        goTrue.invalidateSession()
    }
}



