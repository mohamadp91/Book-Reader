package com.example.bookreader.repository

import android.util.Log
import com.example.bookreader.data.network.BookApi
import com.example.bookreader.models.BookApiModel
import com.example.bookreader.models.BookModel
import com.example.bookreader.models.Item
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookApi: BookApi,
    private val postgrest: Postgrest
) {

    private val tag = "Book Repository"

    suspend fun getBookApiModel(q: String): BookApiModel {
        return withContext(Dispatchers.IO) {
            try {
                bookApi.getBookApiModel(q)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
                throw Exception("An error occurred in fetching book list")
            }
        }
    }

    suspend fun getBookById(bookId: String): Item {
        return withContext(Dispatchers.IO) {
            try {
                bookApi.getBookById(bookId)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
                throw Exception("An error occurred in fetching book with id ${bookId}")
            }
        }
    }

    suspend fun saveBookModelInRemoteDb(bookModel: BookModel) {
        withContext(Dispatchers.IO) {
            try {
                postgrest["book-db"].insert(bookModel)
            } catch (e: Exception) {
                Log.e("book repository", e.message.toString())
                throw Exception("An error occurred")
            }
        }
    }
}