package com.praktikum.trassify.ui.splash

import androidx.compose.foundation.layout.Box
import com.praktikum.trassify.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = viewModel(factory = SplashViewModel.Factory(
    LocalContext.current))) {
    val isLoadingComplete = viewModel.isLoadingComplete.collectAsState(initial = false).value

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    LaunchedEffect(isLoadingComplete) {
        if (isLoadingComplete) {
            navController.navigate("welcome") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Splash Image",
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }


}
