package com.example.bookreader.screens.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookModel
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    suspend fun getBookById(bookId: String): ResultState<*> {
        return try {
            val book = bookRepository.getBookByIdFromRemoteDb(bookId)
            ResultState.Success(book)
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    fun updateBookModel(
        bookModel: BookModel,
        onUpdated: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                bookRepository.updateBookInRemoteDb(bookModel)
                onUpdated.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteBookModel(
        bookId: String,
        onDeleted: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                bookRepository.deleteBookInRemoteDb(bookId)
                onDeleted.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}