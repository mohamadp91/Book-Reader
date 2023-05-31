package com.example.bookreader.screens.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.GoTrue
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val goTrue: GoTrue) : ViewModel() {

    fun checkIfUserLoggedIn(): Boolean {
        val user = goTrue.currentSessionOrNull()?.user
        return user != null
    }
}