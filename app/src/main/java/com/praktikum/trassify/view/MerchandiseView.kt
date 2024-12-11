package com.praktikum.trassify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.composables.MerchandiseCard
import com.praktikum.trassify.composables.ProfileCard
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White

@Composable()
fun MerchandiseView(){
    Box(
        modifier = Modifier.fillMaxSize() .background(
            Brush.radialGradient(
                colors = listOf(
                    Color(0xFFA293FA),
                    Color(0xFF7C66FF)
                ),
                center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                radius = 500f
            )
        ),

        ){
        Column {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Merchandise",
                style = TextType.text25SemiBold,
                color = Color(0xFFDFDCF4),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = White)
                    .padding(20.dp)

            ){
                ProfileCard()
                Spacer(modifier = Modifier.height(20.dp))
                LazyVerticalGrid (
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(10) {
                        MerchandiseCard()
                    }
                }
            }
        }

    }
}