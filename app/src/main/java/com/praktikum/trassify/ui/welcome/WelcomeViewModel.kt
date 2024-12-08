package com.praktikum.trassify.ui.welcome

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.praktikum.trassify.constants.welcomeContent
import com.praktikum.trassify.data.model.WelcomePage
import androidx.navigation.NavController

class WelcomeViewModel(private val navController: NavController? = null) : ViewModel() {
    private val _currentPageIndex = mutableStateOf(0)
    val currentPageIndex: State<Int> get() = _currentPageIndex

    val currentPageContent: WelcomePage
        get() = welcomeContent[_currentPageIndex.value]

    fun nextPage() {
        val nextIndex = _currentPageIndex.value + 1
        if (nextIndex < welcomeContent.size) {
            _currentPageIndex.value = nextIndex
        }
    }

    fun skipToLogin() {
        navController?.navigate("login")
    }

    companion object {
        fun Factory(context: Context, navController: NavController? = null): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WelcomeViewModel(navController)
            }
        }
    }
}