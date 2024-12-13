package com.praktikum.trassify.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import com.praktikum.trassify.R

class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {

    var currentUser: FirebaseUser? = null
        private set

    fun signInWithGoogle(credentialManager: CredentialManager, navController: NavController, context: android.content.Context) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                handleSignIn(result, navController)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Error getting credential", e)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse, navController: NavController) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken, navController)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Invalid google id token", e)
                    }
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, navController: NavController) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    updateUser(auth.currentUser)
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUser(null)
                }
            }
    }

    fun signOut(credentialManager: CredentialManager, navController: NavController) {
        viewModelScope.launch {
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            updateUser(null)
            navController.navigate("login") {
                popUpTo("dashboard") { inclusive = true }
            }
        }
    }

    fun updateUser(user: FirebaseUser?) {
        currentUser = user
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

class AuthViewModelFactory(private val auth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(auth) as T
    }
}