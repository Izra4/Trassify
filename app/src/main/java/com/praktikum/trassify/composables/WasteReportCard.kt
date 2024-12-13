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
    wasteReport: WasteReport // Terima objek WasteReport
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
        // Gambar lingkaran di pojok kanan atas
        Image(
            painter = painterResource(id = R.drawable.circle_schedule),
            contentDescription = "circle",
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(65.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD9D9D9))
            )
        }

        // Konten utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Teks bagian atas: Tampilkan tanggal
            Text(
                text = date, // Gunakan tanggal dari WasteReport
                style = TextType.text16SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = wasteReport.location, // Tampilkan alamat dari WasteReport
                style = TextType.text12Rg,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = time, // Tampilkan waktu dari WasteReport
                style = TextType.text12Md,
                color = Color.White
            )
        }

        // Tombol di pojok kanan bawah
        Button(
            onClick = { /* TODO: Add navigation or action */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .width(90.dp) // Lebar tombol lebih kecil
                .height(32.dp), // Tinggi tombol lebih kecil
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.purple_500)
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp) // Mengurangi padding
        ) {
            Text(
                text = "Lihat detail",
                fontSize = 11.sp, // Ukuran font lebih kecil
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.mont_semi_bold))
            )
        }
    }
}