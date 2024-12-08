package com.praktikum.trassify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.praktikum.trassify.model.CameraRepository

class CameraViewModelFactory(private val cameraRepository: CameraRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            CameraViewModel(cameraRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
