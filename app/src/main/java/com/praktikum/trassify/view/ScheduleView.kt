package com.praktikum.trassify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.composables.DropdownExample
import com.praktikum.trassify.composables.ScheduleCard
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White

@Composable()
fun ScheduleView(){
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
                Spacer(modifier = Modifier.height(90.dp))
                Text(
                    text = "Jadwal Pemungutan Sampah",
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        DropdownExample(
                            options = listOf("Sukun", "Sigura", "Testing"),
                            modifier = Modifier.weight(1f)
                        )
                        DropdownExample(
                            options = listOf("Sukun", "Sigura", "Testing"),
                            modifier = Modifier.weight(1f)
                        )
                        DropdownExample(
                            options = listOf("Sukun", "Sigura", "Testing"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn (verticalArrangement = Arrangement.spacedBy(12.dp)){
                        items(10){
                            ScheduleCard(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

        }
}

@Preview(showBackground = true)
@Composable()
fun ScheduleViewPreview(){
    ScheduleView()
}