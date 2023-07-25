package com.example.bookreader.repository

import android.util.Log
import com.example.bookreader.data.network.BookApi
import com.example.bookreader.models.BookApiModel
import com.example.bookreader.models.BookModel
import com.example.bookreader.models.DocsApiModel
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookApi: BookApi,
    private val postgrest: Postgrest
) {

    private val tag = "Book Repository"

    suspend fun getDocsApi(q: String): DocsApiModel {
        return withContext(Dispatchers.IO) {
            try {
                bookApi.getDocsApi(q)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
                throw Exception("An error occurred in fetching book list")
            }
        }
    }

    suspend fun getBookByWork(work: String): BookApiModel {
        return withContext(Dispatchers.IO) {
            try {
                bookApi.getBookByWork(work)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
                throw Exception("An error occurred in fetching book with id ${work}")
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

    suspend fun getBooksFromRemoteDb(): List<BookModel> {
        return withContext(Dispatchers.IO) {
            try {
                postgrest["book-db"].select().decodeList<BookModel>()
            } catch (e: Exception) {
                throw Exception("An error occurred while fetching the list of book from the database")
            }
        }
    }

    suspend fun getBookByIdFromRemoteDb(bookId: String): BookModel {
        return withContext(Dispatchers.IO) {
            try {
                postgrest["book-db"].select() {
                    BookModel::bookId eq bookId
                }.decodeSingle<BookModel>()
            } catch (e: Exception) {
                throw Exception("An error occurred while fetching the list of book from the database")
            }
        }
    }

    suspend fun updateBookInRemoteDb(bookModel: BookModel) {
        return withContext(Dispatchers.IO) {
            try {
                postgrest["book-db"].update({
                    BookModel::notes setTo bookModel.notes
                    BookModel::startReading setTo bookModel.startReading
                    BookModel::endReading setTo bookModel.endReading
                    BookModel::rate setTo bookModel.rate
                }) {
                    BookModel::bookId eq bookModel.bookId
                }
            } catch (e: Exception) {
                throw Exception("An error occurred while fetching the list of book from the database")
            }
        }
    }

    suspend fun deleteBookInRemoteDb(bookId: String) {
        return withContext(Dispatchers.IO) {
            try {
                postgrest["book-db"].delete {
                    BookModel::bookId eq bookId
                }
            } catch (e: Exception) {
                throw Exception("An error occurred while fetching the list of book from the database")
            }
        }
    }
}