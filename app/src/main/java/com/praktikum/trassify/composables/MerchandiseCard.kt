package com.praktikum.trassify.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.theme.Primary10
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White

@Composable()
fun MerchandiseCard(){
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Article Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "T-Shirt Trassify - Kolaborasi  dengan Pandawara Spesial HUT Kemerdekaan Indonesia T...",
                style = TextType.text8Rg,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(text = "1500 Poin", style = TextType.text8SemiBold, color = Primary10)
                Text(text = "130x Ditukar", style = TextType.text8Rg)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable()
fun MerchandiseCardPreview(){
    MerchandiseCard()
}