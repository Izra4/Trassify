package com.praktikum.trassify.data.remote

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object FirestoreRemote {
    val db = FirebaseDatabase.getInstance("https://trassify-1378a-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    suspend inline fun <reified T> getAllData(collectionName: String): List<T> = try {
        val dataSnapshot = db.child(collectionName).get().await()
        dataSnapshot.children.mapNotNull { it.getValue(T::class.java) }
    } catch (e: Exception) {
        Log.e("FirestoreRemote", "Error while fetching data", e)
        emptyList()
    }

    suspend inline fun <reified T> getDataById(collectionName: String, documentId: String): T? = try {
        val dataSnapshot = db.child(collectionName).child(documentId).get().await()
        dataSnapshot.getValue(T::class.java)
    } catch (e: Exception) {
        Log.e("FirestoreRemote", "Error while fetching data", e)
        null
    }
}
