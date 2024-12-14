package com.praktikum.trassify.data.repository

import android.util.Log
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Merchandise
import com.praktikum.trassify.data.remote.FirebaseRemote

class MerchandiseRepository {
    suspend fun getAllMarchendise(): Response<List<Merchandise>> {
        return try {
            val merchandises = FirebaseRemote.getAllData<Merchandise>("merchandise")
            Log.d("merchandises",merchandises.toString())
            if (merchandises.isEmpty()) {
                Response.Error("merchandise is empty")
            } else {
                Response.Success(merchandises)
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "An unknown error occurred")
        }
    }
}