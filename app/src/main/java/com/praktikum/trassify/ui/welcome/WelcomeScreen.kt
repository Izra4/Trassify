package com.praktikum.trassify.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(navController: NavController, viewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModel.Factory(LocalContext.current))) {
    val currentPageIndex by viewModel.currentPageIndex.observeForever(0)
    val pageContent = viewModel.currentPageContent

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = pageContent.image), // Gunakan gambar sesuai URL atau resource
            contentDescription = pageContent.title
        )

        Text(text = pageContent.title)
        Text(text = pageContent.description)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.previousPage() }) {
            Text(text = "Back")
        }
        Button(onClick = { viewModel.nextPage() }) {
            Text(text = "Next")
        }
    }
}
