package com.praktikum.trassify.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.R
import com.praktikum.trassify.data.model.WasteReport
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.utils.extractDateAndTime

@Composable
fun WasteReportCard(
    modifier: Modifier = Modifier,
    wasteReport: WasteReport,
    onClick: () -> Unit // Tambahkan parameter onClick
) {
    val (date, time) = extractDateAndTime(wasteReport.timestamp)

    Box(
        modifier = modifier
            .width(320.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.purple_500)),
    ) {
        Image(
            painter = painterResource(id = R.drawable.circle_schedule),
            contentDescription = "circle",
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = date,
                style = TextType.text16SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = wasteReport.location,
                style = TextType.text12Rg,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = time,
                style = TextType.text12Md,
                color = Color.White
            )
        }

        Button(
            onClick = onClick, // Gunakan onClick untuk navigasi
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .width(90.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.purple_500)
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "Lihat detail",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.mont_semi_bold))
            )
        }
    }
}