package com.praktikum.trassify.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.praktikum.trassify.ui.theme.AppTypography
import com.praktikum.trassify.ui.theme.gray

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

        Text(text = pageContent.title ,style = AppTypography.h1, modifier = Modifier.padding(vertical = 18.dp) )
        Text(text = pageContent.description, style = AppTypography.text, textAlign = TextAlign.Center, color = gray)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.previousPage() }) {
            Text(text = "Back")
        }
        Button(onClick = { viewModel.nextPage() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(text = "Selanjutnya")
        }
    }
}
