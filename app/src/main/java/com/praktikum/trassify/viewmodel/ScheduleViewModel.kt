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
) : ViewModel() {

    private val _schedules: MutableStateFlow<Response<List<Schedule>>> =
        MutableStateFlow(Response.Idle)
    val schedules: StateFlow<Response<List<Schedule>>> = _schedules

    private val _villages: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val villages: StateFlow<List<String>> = _villages

    private val _subdistricts: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val subdistricts: StateFlow<List<String>> = _subdistricts

    private val _times: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val times: StateFlow<List<String>> = _times

    private var allSchedules: List<Schedule> = emptyList()

    fun getAllSchedules() {
        viewModelScope.launch {
            _schedules.value = Response.Loading
            val response = scheduleRepository.getAllSchedules()
            _schedules.value = response
            if (response is Response.Success) {
                allSchedules = response.data
                updateDropdowns(allSchedules)
            }
        }
    }

    private fun updateDropdowns(schedules: List<Schedule>) {
        val villagesList = schedules.map { it.village }.distinct()
        val subdistrictsList = schedules.map { it.subdistrict }.distinct()
        val timesList = schedules.map { it.timeStamp }.distinct()

        _villages.value = villagesList
        _subdistricts.value = subdistrictsList
        _times.value = timesList
    }

    fun filterSchedules(village: String, subdistrict: String, time: String) {
        viewModelScope.launch {
            _schedules.value = Response.Loading
            val filteredSchedules = allSchedules.filter {
                (village.isEmpty() || it.village == village) &&
                        (subdistrict.isEmpty() || it.subdistrict == subdistrict) &&
                        (time.isEmpty() || it.timeStamp == time)
            }
            _schedules.value = Response.Success(filteredSchedules)
        }
    }
}

class ScheduleViewModelFactory(
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(
                scheduleRepository = scheduleRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

