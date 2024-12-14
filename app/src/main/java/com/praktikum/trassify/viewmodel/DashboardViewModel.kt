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

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val merchandiseRepository: MerchandiseRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel(){


    private val _userProfile: MutableStateFlow<Response<User?>> = MutableStateFlow(Response.Idle)
    val userProfile : StateFlow<Response<User?>> = _userProfile

    private val _articles: MutableStateFlow<Response<List<Article>>> = MutableStateFlow(Response.Idle)
    val articles : StateFlow<Response<List<Article>>> = _articles

    private val _merchandises: MutableStateFlow<Response<List<Merchandise>>> = MutableStateFlow(Response.Idle)
    val merchandises : StateFlow<Response<List<Merchandise>>> = _merchandises

    private val _schedules: MutableStateFlow<Response<List<Schedule>>> = MutableStateFlow(Response.Idle)
    val schedules : StateFlow<Response<List<Schedule>>> = _schedules




    fun userProfile(userId : String){
        viewModelScope.launch {
            _userProfile.value = Response.Loading
            val response = userRepository.getDashboardProfile(userId)
            _userProfile.value = response
        }
    }

    fun getAllArticle(){
        viewModelScope.launch {
            _articles.value = Response.Loading
            val response = articleRepository.getAllArticle()
            _articles.value = response
        }
    }

    fun getAllMerchandise(){
        viewModelScope.launch {
            _merchandises.value = Response.Loading
            val response = merchandiseRepository.getAllMarchendise()
            _merchandises.value = response
        }
    }

    fun getAllSchedules(){
        viewModelScope.launch {
            _schedules.value = Response.Loading
            val response = scheduleRepository.getAllSchedules()
            _schedules.value = response
        }
    }

//    companion object {
//        fun Factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val userRepository = UserRepository()
//                val articleRepository = ArticleRepository()
//                val merchandiseRepository = MerchandiseRepository()
//                val scheduleRepository = ScheduleRepository()
//                DashboardViewModel(
//                    userRepository = userRepository,
//                    articleRepository = articleRepository,
//                    merchandiseRepository = merchandiseRepository,
//                    scheduleRepository = scheduleRepository
//
//                )
//            }
//        }
//    }
}

class DashboardViewModelFactory(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val merchandiseRepository: MerchandiseRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(
                userRepository = userRepository,
                articleRepository = articleRepository,
                merchandiseRepository = merchandiseRepository,
                scheduleRepository = scheduleRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
