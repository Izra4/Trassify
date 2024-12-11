package com.praktikum.trassify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.R
import com.praktikum.trassify.composables.ProfileCard
import com.praktikum.trassify.ui.theme.TextType

@Composable()
fun DashboardView(){
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()){
            Image(painter = painterResource(id = R.drawable.circle_dashboard), contentDescription = "circle", modifier =Modifier.fillMaxWidth())
            Column(verticalArrangement = Arrangement.Bottom) {
                Text(text = "Selamat Siang, Nabih!", style = TextType.text25SemiBold, color = Color(0xFFDFDCF4))
                Spacer(modifier = Modifier.height(12.dp))
                ProfileCard()
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable()
fun DashboardViewPreview(){
    DashboardView()
}