package com.praktikum.trassify.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(navController: NavController, viewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModel.Factory(LocalContext.current))) {
    val pageContent = viewModel.currentPageContent

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = pageContent.image),
            contentDescription = pageContent.title,
            modifier = Modifier.width(300.dp).height(300.dp),

        )

        Text(text = pageContent.title, fontSize = 20.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, fontFamily = )
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
