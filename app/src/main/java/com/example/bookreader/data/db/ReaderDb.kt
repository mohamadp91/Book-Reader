package com.example.bookreader.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookreader.models.UserModel


@Database(entities = [UserModel::class], version = 1, exportSchema = false)
abstract class ReaderDb : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ReaderDb? = null

        fun getInstance(context: Context): ReaderDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReaderDb::class.java,
                    "app_database"
                ).build().also { INSTANCE = it }
            }
        }

    }
}