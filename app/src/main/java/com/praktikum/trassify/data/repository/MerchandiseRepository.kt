package com.praktikum.trassify.data.repository

import android.util.Log
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Merchandise
import com.praktikum.trassify.data.remote.FirestoreRemote

class MerchandiseRepository {
    suspend fun getAllMarchendise(): Response<List<Merchandise>> {
        return try {
            val merchandises = FirestoreRemote.getAllData<Merchandise>("merchandises")
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