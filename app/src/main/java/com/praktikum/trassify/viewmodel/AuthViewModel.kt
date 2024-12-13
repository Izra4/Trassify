package com.praktikum.trassify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModelProvider
import com.praktikum.trassify.R
import com.praktikum.trassify.data.model.User
import com.praktikum.trassify.data.repository.UserRepository

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState = _signInState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    sealed class SignInState {
        object Idle : SignInState()
        object Loading : SignInState()
        object Success : SignInState()
        data class Error(val message: String) : SignInState()
    }

    init {
        auth.currentUser?.let { firebaseUser ->
            viewModelScope.launch {
                userRepository.observeUserChanges(firebaseUser.uid)
                    .collect { user ->
                        _currentUser.value = user
                    }
            }
        }
    }

    fun signInWithGoogle(credentialManager: CredentialManager, navController: NavController, context: android.content.Context) {
        _signInState.value = SignInState.Loading
        Log.d(TAG, "Starting Google Sign In process")

        val clientId = context.getString(R.string.web_client_id)
        Log.d(TAG, "Using Web Client ID: $clientId")

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                Log.d(TAG, "Requesting credential from Credential Manager")
                val result = credentialManager.getCredential(context, request)
                Log.d(TAG, "Successfully received credential")
                handleSignIn(result, navController)
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is NoCredentialException -> "No Google account found or user cancelled"
                    is GetCredentialException -> "Failed to get credentials"
                    else -> "Unexpected error: ${e.message}"
                }
                Log.e(TAG, errorMessage, e)
                _signInState.value = SignInState.Error(errorMessage)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse, navController: NavController) {
        try {
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken, navController)
                    } else {
                        _signInState.value = SignInState.Error("Unsupported credential type")
                    }
                }
                else -> {
                    _signInState.value = SignInState.Error("Invalid credential format")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing credential", e)
            _signInState.value = SignInState.Error("Failed to process sign-in credential")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, navController: NavController) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                viewModelScope.launch {
                    try {
                        // Simpan atau update user data
                        val user = userRepository.createOrUpdateUser(authResult.user!!)
                        _currentUser.value = user
                        _signInState.value = SignInState.Success
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error saving user data", e)
                        _signInState.value = SignInState.Error("Failed to save user data")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "signInWithCredential:failure", e)
                _currentUser.value = null
                _signInState.value = SignInState.Error("Authentication failed: ${e.message}")
            }
    }

    fun signOut(credentialManager: CredentialManager, navController: NavController) {
        viewModelScope.launch {
            try {
                auth.currentUser?.let { user ->
                    // Update last login sebelum sign out
                    userRepository.createOrUpdateUser(user)
                }
                auth.signOut()
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                _currentUser.value = null
                _signInState.value = SignInState.Idle
                navController.navigate("login") {
                    popUpTo("dashboard") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during sign out", e)
                _signInState.value = SignInState.Error("Failed to sign out")
            }
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}

class AuthViewModelFactory(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(auth, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}