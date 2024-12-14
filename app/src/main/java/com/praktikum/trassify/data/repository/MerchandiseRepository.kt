package com.praktikum.trassify.data.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Merchandise
import kotlinx.coroutines.tasks.await

class MerchandiseRepository(
    private val database: DatabaseReference
) {
    private val merchandiseRef = database.child("merchandise")

    suspend fun getAllMarchendise(): Response<List<Merchandise>> {
        return try {
            // Ambil snapshot dari node "merchandise"
            val snapshot = merchandiseRef.get().await()
            val merchandises = mutableListOf<Merchandise>()
            for (child in snapshot.children) {
                child.getValue<Merchandise>()?.let {
                    merchandises.add(it)
                }
            }

            Log.d("merchandises", merchandises.toString())

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
