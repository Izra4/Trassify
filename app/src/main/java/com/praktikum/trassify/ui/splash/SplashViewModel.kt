package com.praktikum.trassify.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel() : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoadingComplete: StateFlow<Boolean> = _isLoading

    fun loadInitialData() {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value = true
        }
    }

    companion object {
        fun Factory(context : Context) : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SplashViewModel()
            }
        }
    }
}
