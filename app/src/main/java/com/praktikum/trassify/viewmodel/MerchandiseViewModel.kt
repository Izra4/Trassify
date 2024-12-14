package com.praktikum.trassify.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Article
import com.praktikum.trassify.data.model.Merchandise
import com.praktikum.trassify.data.model.Schedule
import com.praktikum.trassify.data.model.User
import com.praktikum.trassify.data.repository.ArticleRepository
import com.praktikum.trassify.data.repository.MerchandiseRepository
import com.praktikum.trassify.data.repository.ScheduleRepository
import com.praktikum.trassify.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MerchandiseViewModel(
    private val userRepository: UserRepository,
    private val merchandiseRepository: MerchandiseRepository,
) : ViewModel() {


    private val _userProfile: MutableStateFlow<Response<User?>> = MutableStateFlow(Response.Idle)
    val userProfile: StateFlow<Response<User?>> = _userProfile

    private val _merchandises: MutableStateFlow<Response<List<Merchandise>>> =
        MutableStateFlow(Response.Idle)
    val merchandises: StateFlow<Response<List<Merchandise>>> = _merchandises

    private val _merchandiseDetail: MutableStateFlow<Response<Merchandise>> = MutableStateFlow(Response.Idle)
    val merchandiseDetail: StateFlow<Response<Merchandise>> = _merchandiseDetail

    fun userProfile(userId: String) {
        viewModelScope.launch {
            _userProfile.value = Response.Loading
            val response = userRepository.getDashboardProfile(userId)
            _userProfile.value = response
        }
    }


    fun getAllMerchandise() {
        viewModelScope.launch {
            _merchandises.value = Response.Loading
            val response = merchandiseRepository.getAllMarchendise()
            _merchandises.value = response
        }
    }

    fun fetchMerchDetail(merchId: String) {
        viewModelScope.launch {
            _merchandiseDetail.value = Response.Loading
            val response = merchandiseRepository.getMerchandiseDetail(merchId)
            _merchandiseDetail.value = response
        }
    }
}

class MerchandiseViewModelFactory(
    private val userRepository: UserRepository,
    private val merchandiseRepository: MerchandiseRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MerchandiseViewModel::class.java)) {
            return MerchandiseViewModel(
                userRepository = userRepository,
                merchandiseRepository = merchandiseRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}