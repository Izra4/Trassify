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

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel(){

    private val _schedules: MutableStateFlow<Response<List<Schedule>>> = MutableStateFlow(Response.Idle)
    val schedules : StateFlow<Response<List<Schedule>>> = _schedules

    fun getAllSchedules(){
        viewModelScope.launch {
            _schedules.value = Response.Loading
            val response = scheduleRepository.getAllSchedules()
            _schedules.value = response
        }
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val scheduleRepository = ScheduleRepository()
                ScheduleViewModel(
                    scheduleRepository = scheduleRepository

                )
            }
        }
    }

}