package com.praktikum.trassify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.R
import com.praktikum.trassify.composables.ArticleCard
import com.praktikum.trassify.composables.MerchandiseCard
import com.praktikum.trassify.composables.ProfileCard
import com.praktikum.trassify.composables.ScheduleCard
import com.praktikum.trassify.ui.theme.TextType

@Composable()
fun DashboardView(){
    Column(modifier = Modifier.fillMaxWidth()) {
        Box{
            Image(
                painter = painterResource(id = R.drawable.circle_dashboard),
                contentDescription = "circle",
                modifier =Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 40.dp, end = 20.dp)
            ) {
                Text(text = "Selamat Siang, Nabih!", style = TextType.text25SemiBold, color = Color(0xFFDFDCF4))
                Spacer(modifier = Modifier.height(12.dp))
                ProfileCard()
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Artikel Terkini",
                style =  TextType.text15SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(5){
                    ArticleCard()
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Merchandise Populer",
                style =  TextType.text15SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(5){
                    MerchandiseCard()
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Jadwal Pungut Sampah Kamu",
                style =  TextType.text15SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow ( horizontalArrangement = Arrangement.spacedBy(12.dp)){
                items(5){
                    ScheduleCard()
                }
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