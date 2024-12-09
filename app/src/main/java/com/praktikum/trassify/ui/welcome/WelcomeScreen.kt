package com.praktikum.trassify.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.praktikum.trassify.ui.theme.Gray20

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModel.Factory(LocalContext.current, navController))
) {
    val pageContent = viewModel.currentPageContent
    val currentIndex = viewModel.currentPageIndex.value
    val totalPages = viewModel.totalPages

    Column {
        if (currentIndex > 0) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.only_logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .padding(top=5.dp),
                )
                if(currentIndex < totalPages -1){
                    Image(
                        painter = painterResource(id = R.drawable.skip_button),
                        contentDescription = "skip button",
                        modifier = Modifier
                            .width(170.dp)
                            .height(100.dp)
                            .padding(start = 25.dp, bottom= 7.dp)
                            .clickable { viewModel.navigateToLogin() },
                    )
                }else{
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "back button",
                        modifier = Modifier
                            .width(170.dp)
                            .height(100.dp)
                            .padding(start = 25.dp, bottom= 7.dp)
                            .clickable { viewModel.backToWelcome() },
                    )
                }

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
                    .width(350.dp)
                    .height(350.dp),
            )

            Text(
                text = pageContent.title,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.monserrat_black)),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 25.sp,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
            )
            Text(
                text = pageContent.description,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Gray10
            )
            if(currentIndex > 0){
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    repeat(totalPages-1) { index ->
                        val color = if (index == currentIndex-1) Primary10 else Gray20
                        Spacer(
                            modifier = Modifier
                                .width(15.dp)
                                .height(4.dp)
                                .padding(horizontal = 4.dp)
                                .background(color, shape = RoundedCornerShape(30))
                        )
                    }
                }

            }

            if(currentIndex == 0){
                Spacer(
                    modifier = Modifier.height(32.dp)
                )
            }
            Button(
                onClick = {  if (currentIndex < totalPages - 1) {
                    viewModel.nextPage()
                } else {
                    viewModel.navigateToLogin()
                } },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary10,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = if(currentIndex < totalPages -1 )"Selanjutnya" else "Memulai",
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }
}