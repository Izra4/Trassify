package com.praktikum.trassify.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.User
import com.praktikum.trassify.data.remote.FirebaseRemote
import com.praktikum.trassify.utils.formatTimestamp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val database: DatabaseReference,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val usersRef = database.child("users")

    suspend fun createOrUpdateUser(firebaseUser: FirebaseUser): User {
        val user = User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            name = firebaseUser.displayName ?: "",
            imageUrl = firebaseUser.photoUrl?.toString() ?: "",
            timestamp = formatTimestamp(System.currentTimeMillis())
        )

        return try {
            // Cek apakah user sudah ada
            val existingUser = getUserById(user.id)
            if (existingUser == null) {
                // User baru
                usersRef.child(user.id).setValue(user).await()
                user
            } else {
                // Update data user yang ada
                val updates = mapOf(
                    "timestamp" to user.timestamp,
                    "email" to user.email,
                    "name" to user.name,
                    "imageUrl" to user.imageUrl
                )
                usersRef.child(user.id).updateChildren(updates).await()
                existingUser.copy(
                    timestamp = user.timestamp,
                    email = user.email,
                    name = user.name,
                    imageUrl = user.imageUrl
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUserById(uid: String): User? {
        return try {
            val snapshot = usersRef.child(uid).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteUser(uid: String) {
        try {
            usersRef.child(uid).removeValue().await()
        } catch (e: Exception) {
            throw e
        }
    }

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

    fun observeUserChanges(uid: String): Flow<User?> = callbackFlow {
        val listener = usersRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })

        awaitClose {
            usersRef.child(uid).removeEventListener(listener)
        }
    }
}
