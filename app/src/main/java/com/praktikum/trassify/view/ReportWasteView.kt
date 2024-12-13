package com.praktikum.trassify.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.R
import com.praktikum.trassify.viewmodel.CameraViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.praktikum.trassify.data.model.WasteReport
import com.praktikum.trassify.viewmodel.LocationViewModel
import com.praktikum.trassify.viewmodel.WasteReportViewModel

@Composable
fun ReportWastePage(
    cameraViewModel: CameraViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel(),
    wasteReportViewModel: WasteReportViewModel = viewModel() // Tambahkan ViewModel untuk WasteReport
) {
    var catatan by remember { mutableStateOf("") }
    val imageUri by cameraViewModel.imageUri
    val location by locationViewModel.locationState

    val context = LocalContext.current

    // Mendapatkan userId dari FirebaseAuth
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    LaunchedEffect(Unit) {
        locationViewModel.getLocation(context = context)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(color = colorResource(id = R.color.purple_500))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 36.dp)
            ) {
                // Title/Header
                Text(
                    text = "Laporkan Sampah",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.mont_semi_bold)),
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)
                )

                Column(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(16.dp)
                ) {
                    TextField(
                        value = location,
                        onValueChange = {}, // Lokasi diambil dari LocationViewModel, jadi tidak perlu diubah manual
                        label = { Text("Lokasi") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .shadow(4.dp, RoundedCornerShape(12.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = colorResource(id = R.color.purple_500),
                            focusedIndicatorColor = colorResource(id = R.color.purple_500),
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = colorResource(id = R.color.purple_500),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = catatan,
                        onValueChange = { catatan = it },
                        label = { Text("Catatan") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .shadow(4.dp, RoundedCornerShape(12.dp)),
                        singleLine = false,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = colorResource(id = R.color.purple_500),
                            focusedIndicatorColor = colorResource(id = R.color.purple_500),
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = colorResource(id = R.color.purple_500),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Dokumentasi Laporan:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.mont_md)),
                        color = colorResource(id = R.color.purple_200),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Image Preview
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Gambar Sampah",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Push button ke bawah

                    Button(
                        onClick = {
                            // Validasi dan Kirim Data
                            if (location.isNotBlank() && catatan.isNotBlank() && userId != null) {
                                val timestamp = System.currentTimeMillis().toString()
                                val wasteReport = WasteReport(
                                    id = "", // Akan diisi oleh Repository
                                    userId = userId,
                                    location = location,
                                    note = catatan,
                                    status = "Pending",
                                    timestamp = timestamp
                                )

                                wasteReportViewModel.saveWasteReport(
                                    wasteReport = wasteReport,
                                    imageUri = imageUri!!,
                                    context = context,
                                    bucket = "waste_image",
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Laporan berhasil disimpan!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = { error ->
                                        Toast.makeText(
                                            context,
                                            "Gagal menyimpan laporan: ${error.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Pastikan lokasi dan catatan diisi dengan benar.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.purple_500),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Laporkan",
                            fontFamily = FontFamily(Font(R.font.mont_semi_bold)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}