package com.praktikum.trassify.data.repository

import android.util.Log
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.User
import com.praktikum.trassify.data.remote.FirebaseRemote

class UserRepository{
    suspend fun getDashboardProfile(id: String): Response<User?> {
        return try {
            val user = FirebaseRemote.getDataById<User>("users", id)
            if (user != null) {
                Response.Success(user)
            } else {
                Response.Error("User with id $id not found")
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "An unknown error occurred")
        }
    }

}