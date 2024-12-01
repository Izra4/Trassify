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

class WelcomeViewModel : ViewModel() {
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

    fun previousPage() {
        val prevIndex = _currentPageIndex.value - 1
        if (prevIndex >= 0) {
            _currentPageIndex.value = prevIndex
        }
    }

    companion object {
        fun Factory(context : Context) : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WelcomeViewModel()
            }
        }
    }
}
