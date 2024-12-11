package com.praktikum.trassify.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.ui.theme.Primary10
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White

@Composable()
fun ScheduleCard(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .width(320.dp)
            .height(105.dp)
            .clip(RoundedCornerShape(10.dp))
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
            .background(color = Primary10),
    ){
        Image(
            painter = painterResource(id = R.drawable.circle_schedule),
            contentDescription = "circle",
            modifier = Modifier.align(Alignment.TopEnd))
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .width(4.dp)
                .height(65.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Color(0xFFD9D9D9)
                ) )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxSize()

            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Lowokwaru", style = TextType.text16SemiBold, color = White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "08:00 - 09:30", style = TextType.text12Md, color = White)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text="Kelurahan Harapan jaya", style = TextType.text12Rg, color= White)
                Text(text="Senin, 17 Agustus 2024", style = TextType.text10SemiBold, color = White, modifier = Modifier.align(Alignment.End).padding(top = 16.dp, bottom = 12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable()
fun ScheduleCardPreview(){
    ScheduleCard()
}