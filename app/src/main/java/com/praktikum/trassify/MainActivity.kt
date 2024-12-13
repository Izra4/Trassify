package com.praktikum.trassify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.praktikum.trassify.ui.screens.LoginScreen
import com.praktikum.trassify.ui.screens.DashboardScreen
import com.praktikum.trassify.ui.screens.RegisterScreen
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.view.WelcomeScreen
import com.praktikum.trassify.ui.viewmodel.LoginViewModel
import com.praktikum.trassify.ui.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.launch
import androidx.credentials.CredentialManager
import com.praktikum.trassify.R

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(auth)).get(LoginViewModel::class.java)

        setContent {
            TrassifyTheme {
                val navController = rememberNavController()

                val currentUser = loginViewModel.currentUser

                NavHost(
                    navController = navController,
                    startDestination = if (currentUser != null) "dashboard" else "welcome"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            onGoogleSignInClick = { signInWithGoogle(navController) }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            user = currentUser,
                            onSignOutClick = { signOut(navController) }
                        )
                    }
                    composable("welcome") {
                        WelcomeScreen(navController = navController)
                    }
                    composable("register") {
                        RegisterScreen(
                            navController = navController,
                            onGoogleSignInClick = { signInWithGoogle(navController) }
                        )
                    }
                }
            }
        }
    }

    private fun signInWithGoogle(navController: NavController) {
        val credentialManager = CredentialManager.create(this)

        loginViewModel.signInWithGoogle(
            credentialManager = credentialManager,
            navController = navController,
            context = this
        )
    }

    private fun signOut(navController: NavController) {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@MainActivity)
            loginViewModel.signOut(credentialManager, navController)
        }
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.updateUser(auth.currentUser)
    }
}
