package com.praktikum.trassify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.praktikum.trassify.R
import com.praktikum.trassify.data.model.WasteReport
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.viewmodel.WasteReportViewModel

@Composable
fun WasteReportHistoryDetailPage(
    reportId: String,
    wasteReportViewModel: WasteReportViewModel = viewModel()
) {
    // State untuk menyimpan data laporan
    val wasteReportState = remember { mutableStateOf<WasteReport?>(null) }
    val errorState = remember { mutableStateOf<String?>(null) }

    // Memuat detail laporan saat composable pertama kali dijalankan
    LaunchedEffect(reportId) {
        wasteReportViewModel.fetchWasteReportDetail(
            reportId = reportId,
            onSuccess = { report ->
                wasteReportState.value = report
            },
            onError = { exception ->
                errorState.value = exception.message
            }
        )
    }

    // Scaffold untuk layout halaman detail
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (errorState.value != null) {
            // Tampilkan error jika ada masalah
            Text(
                text = "Error: ${errorState.value}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else if (wasteReportState.value == null) {
            // Tampilkan loading state
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Tampilkan data laporan
            val wasteReport = wasteReportState.value!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.purple_500))
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                // Header Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp, bottom = 16.dp, start = 16.dp)
                ) {
                    Text(
                        text = "Riwayat Laporan",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.mont_bold)),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                // Main Content Section
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .padding(16.dp)
                ) {
                    // Tanggal Laporan
                    Text(
                        text = wasteReport.timestamp ?: "Tanggal tidak tersedia",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.End)
                    )

                    // Lokasi Sampah
                    Text(
                        text = "Lokasi Sampah:",
                        style = TextType.text14SemiBold,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = wasteReport.location ?: "Lokasi tidak tersedia",
                        style = TextType.text13Rg,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Catatan
                    Text(
                        text = "Catatan:",
                        style = TextType.text14SemiBold,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = wasteReport.note ?: "Catatan tidak tersedia",
                        style = TextType.text13Rg,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dokumentasi Laporan
                    Text(
                        text = "Dokumentasi Laporan:",
                        style = TextType.text14SemiBold,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(wasteReport.photoReport),
                        contentDescription = "Dokumentasi Laporan",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dokumentasi Diambil
                    Text(
                        text = "Dokumentasi Diambil:",
                        style = TextType.text14SemiBold,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(wasteReport.photoResponse),
                        contentDescription = "Dokumentasi Laporan",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WasteReportHistoryDetailPagePreview() {
    WasteReportHistoryDetailPage(reportId = "exampleReportId")
}

