package com.example.bookreader.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.DocsApiModel
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    private val tag = "Search View Model"
    private var _booksResults: MutableState<ResultState<*>> = mutableStateOf(ResultState.IDLE)
    val result get() = _booksResults
    fun getDocsApi(q: String) {
        viewModelScope.launch {
            _booksResults.value = ResultState.Loading
            try {
                val docsApiModel = bookRepository.getDocsApi(q)
                _booksResults.value = ResultState.Success<DocsApiModel>(docsApiModel)
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
                _booksResults.value = ResultState.Error(e)
            }
        }
    }
}