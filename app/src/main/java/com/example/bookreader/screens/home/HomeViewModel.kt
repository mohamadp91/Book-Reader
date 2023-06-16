package com.example.bookreader.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.models.UserModel
import com.example.bookreader.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _user: MutableStateFlow<UserModel?> = MutableStateFlow(null)
    val user get() = _user
    private val tag = "HomeViewModel"

    init {
        getUserById()
    }

    private fun getUserById() {
        viewModelScope.launch {
            userRepository.getUserByIdLocally().distinctUntilChanged().collectLatest {
                if (it.isNotEmpty())
                    _user.emit(it.first())
            }
        }
    }

    fun logout(user: UserModel) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(user)
                userRepository.invalidateSession()
            } catch (e: Exception) {
                Log.e(tag, e.message.toString())
            }
        }
    }
}