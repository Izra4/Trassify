package com.praktikum.trassify.ui.welcome


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.praktikum.trassify.ui.theme.Gray10
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.theme.Primary10
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun WelcomeScreen(navController: NavController, viewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModel.Factory(LocalContext.current, navController))) {
    val pageContent = viewModel.currentPageContent
    val indexContent = viewModel.currentPageIndex.value

    Column {
        if (indexContent > 0) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                )
                Image(
                    painter = painterResource(id = R.drawable.skip),
                    contentDescription = "skip button",
                    modifier = Modifier
                        .width(170.dp)
                        .height(100.dp)
                        .padding(end = 8.dp)
                        .clickable { viewModel.skipToLogin() }
                    ,
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = pageContent.image),
                contentDescription = pageContent.title,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
            )

            Text(
                text = pageContent.title,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
            )
            Text(
                text = pageContent.description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Gray10
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.nextPage() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary10,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp) // Added border radius of 12.dp
            ) {
                Text(
                    text = "Selanjutnya",
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }
    }
}
