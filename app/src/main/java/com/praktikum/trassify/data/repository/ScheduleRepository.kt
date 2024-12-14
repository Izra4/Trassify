package com.praktikum.trassify.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Schedule
import kotlinx.coroutines.tasks.await

class ScheduleRepository(
    private val database: DatabaseReference
) {
    private val schedulesRef = database.child("schedules")

    suspend fun getAllSchedules(): Response<List<Schedule>> {
        return try {
            // Ambil snapshot dari node "schedules"
            val snapshot = schedulesRef.get().await()
            val schedules = mutableListOf<Schedule>()
            for (child in snapshot.children) {
                child.getValue<Schedule>()?.let {
                    schedules.add(it)
                }
            }

            if (schedules.isEmpty()) {
                Response.Error("schedules is empty")
            } else {
                Response.Success(schedules)
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "An unknown error occurred")
        }
    }
}
