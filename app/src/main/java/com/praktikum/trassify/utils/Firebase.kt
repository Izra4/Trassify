package com.praktikum.trassify.utils

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

object FirebaseUtils {
    fun signInWithGoogle(navController: NavController, auth: FirebaseAuth) {
        val credential = GoogleAuthProvider.getCredential("idToken", null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseUtils", "signInWithCredential:success")
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Log.e("FirebaseUtils", "signInWithCredential:failure", task.exception)
            }
        }
    }
}
