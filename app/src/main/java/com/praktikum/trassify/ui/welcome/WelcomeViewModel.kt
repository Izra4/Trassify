package com.praktikum.trassify.ui.welcome

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.praktikum.trassify.data.model.WelcomePage
import com.praktikum.trassify.constants.welcomeContent

class WelcomeViewModel () : ViewModel() {
    private val _currentPageIndex = MutableLiveData(0)
    val currentPageIndex: LiveData<Int> get() = _currentPageIndex


    val currentPageContent: WelcomePage
        get() = welcomeContent[currentPageIndex.value ?: 0]

    fun nextPage() {
        val nextIndex = (_currentPageIndex.value ?: 0) + 1
        print(nextIndex)
        if (nextIndex < welcomeContent.size) {
            _currentPageIndex.value = nextIndex
        }
    }

    fun previousPage() {
        val prevIndex = (_currentPageIndex.value ?: 0) - 1
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