// AuthManager.kt
package com.praktikum.trassify.auth

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.praktikum.trassify.utils.FirebaseUtils
import kotlinx.coroutines.launch

class AuthManager {
    private val auth: FirebaseAuth = Firebase.auth

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signInWithGoogle(navController: NavController) {
        FirebaseUtils.signInWithGoogle(navController, auth)
    }

    fun signOut(navController: NavController) {
        auth.signOut()
        navController.navigate("login") {
            popUpTo("dashboard") { inclusive = true }
        }
    }
}
