package com.example.bookreader.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookModel
import com.example.bookreader.models.Item
import com.example.bookreader.repository.BookRepository
import com.example.bookreader.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

    suspend fun getBookById(bookId: String): ResultState<*> {
        return try {
            val item = bookRepository.getBookApiModelById(bookId)
            ResultState.Success(item)
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    fun saveBookInDb(item: Item, onUserSaved: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.getUserByIdLocally().collectLatest {
                    if (it.isNotEmpty()) {
                        val bookData = item.volumeInfo
                        val bookModel = BookModel(
                            bookId = item.id,
                            title = bookData.title,
                            authors = bookData.authors,
                            description = bookData.description,
                            categories = bookData.categories,
                            pageCount = item.volumeInfo.pageCount,
                            publishedDate = bookData.publishedDate,
                            rate = bookData.averageRating,
                            imageUrl = bookData.imageLinks?.smallThumbnail,
                            userId = it.first().userId
                        )
                        bookRepository.saveBookModelInRemoteDb(bookModel)
                        onUserSaved.invoke()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}