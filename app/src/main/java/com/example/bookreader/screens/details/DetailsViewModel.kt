package com.example.bookreader.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookApiModel
import com.example.bookreader.models.BookModel
import com.example.bookreader.models.DescriptionObject
import com.example.bookreader.repository.BookRepository
import com.example.bookreader.repository.UserRepository
import com.example.bookreader.util.getImageUrlById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

    suspend fun getBookByWork(work: String): ResultState<*> {
        return try {
            val item = bookRepository.getBookByWork(work)
            ResultState.Success(item)
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    fun saveBookInDb(
        book: BookApiModel,
        onUserSaved: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                userRepository.getUserByIdLocally().collectLatest {
                    val work = book.key.substringAfter("/works/")
                    if (it.isNotEmpty()) {
                        val bookModel = BookModel(
                            bookId = work,
                            title = book.title,
                            // todo : authors
//                            authors = book.authors.,
                            description = DescriptionObject.getBookDescription(book.description),
                            categories = book.subjects,
                            publishedDate = LocalDateTime.parse(book.created.value).date.toString(),
                            imageUrl = getImageUrlById(book.covers?.first()?.toString() ?: ""),
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