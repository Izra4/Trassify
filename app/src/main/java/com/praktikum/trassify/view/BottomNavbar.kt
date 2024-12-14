package com.praktikum.trassify.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onCameraClick: () -> Unit,
    isCameraSelected: Boolean // Tambahkan parameter untuk status klik kamera
) {
    val purpleColor = colorResource(id = R.color.purple_500)
    val whiteColor = colorResource(id = R.color.white)
    val grayColor = Color.Gray

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Navbar dengan shadow dan rounded corners
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            containerColor = Color.White,
            tonalElevation = 12.dp
        ) {
            // Home Navigation Item
            NavigationBarItem(
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = "Home",
                        tint = if (selectedIndex == 0) purpleColor else grayColor
                    )
                },
            )

            // Jadwal Navigation Item
            NavigationBarItem(
                modifier = Modifier.padding(end = 38.dp),
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Jadwal",
                        tint = if (selectedIndex == 1) purpleColor else grayColor
                    )
                }
            )

            // Berita Navigation Item
            NavigationBarItem(
                modifier = Modifier.padding(start = 38.dp),
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_newspaper),
                        contentDescription = "Camera",
                        tint = if (selectedIndex == 2) purpleColor else grayColor
                    )
                }
            )

            // Merchandise Navigation Item
            NavigationBarItem(
                selected = selectedIndex == 3,
                onClick = { onItemSelected(3) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_store),
                        contentDescription = "Article",
                        tint = if (selectedIndex == 3) purpleColor else grayColor
                    )
                }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-32).dp)
        ) {
            // Background dengan Canvas untuk membuat diamond shape custom dan shadow manual
            Box(
                modifier = Modifier
                    .size(76.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val size = size.minDimension
                    val path = Path().apply {
                        moveTo(size / 2f, 0f) // Atas
                        lineTo(0f, size / 2f) // Kiri
                        lineTo(size / 2f, size) // Bawah
                        lineTo(size, size / 2f) // Kanan
                        close() // Menutup shape
                    }

                    // Shadow
                    drawPath(
                        path = path,
                        color = Color.Black.copy(alpha = 0.3f),
                        style = Fill
                    )
                    translate(left = 4f, top = 4f) {
                        drawPath(
                            path = path,
                            color = Color.Black.copy(alpha = 0.2f),
                            style = Fill
                        )
                    }

                    // Gambar diamond
                    drawPath(
                        path = path,
                        color = whiteColor
                    )
                }

                // Menempatkan IconButton di atas background diamond yang sudah diputar
                IconButton(
                    onClick = {
                        onCameraClick() // Panggil onCameraClick saat tombol diklik
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Camera",
                        tint = if (isCameraSelected) purpleColor else grayColor // Warna ungu jika tombol kamera dipilih
                    )
                }
            }
        }
    }
}
