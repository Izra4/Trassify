package com.praktikum.trassify.data.repository

import android.util.Log
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Merchandise
import com.praktikum.trassify.data.model.Schedule
import com.praktikum.trassify.data.remote.FirestoreRemote

class ScheduleRepository {
    suspend fun getAllSchedules(): Response<List<Schedule>> {
        return try {
            val schedules = FirestoreRemote.getAllData<Schedule>("schedules")
            if (schedules.isEmpty()) {
                Response.Error("merchandise is empty")
            } else {
                Response.Success(schedules)
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "An unknown error occurred")
        }
    }
}