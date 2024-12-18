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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.praktikum.trassify.composables.DropdownExample
import com.praktikum.trassify.composables.WasteReportCard
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White
import com.praktikum.trassify.viewmodel.WasteReportViewModel

@Composable
fun WasteReportHistoryView(
    navController: NavController,
    wasteReportViewModel: WasteReportViewModel = viewModel()
) {
    // Ambil daftar laporan terfilter dan status filter aktif
    val wasteReports by wasteReportViewModel.filteredWasteReports
    val currentFilter by wasteReportViewModel.currentFilter

    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Mendapatkan userId dari FirebaseAuth
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    // Ambil data laporan saat halaman dimuat
    LaunchedEffect(userId) {
        if (userId != null) {
            wasteReportViewModel.fetchWasteReports(
                userId = userId,
                onSuccess = {
                    errorMessage.value = null
                },
                onError = { exception ->
                    errorMessage.value = "Gagal mengambil data laporan: ${exception.message}"
                }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.radialGradient(
                colors = listOf(
                    Color(0xFFA293FA),
                    Color(0xFF7C66FF)
                ),
                center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                radius = 500f
            )
        )
    ) {
        Column {
            Spacer(modifier = Modifier.height(90.dp))
            Text(
                text = "Riwayat Laporan",
                style = TextType.text25SemiBold,
                color = Color(0xFFDFDCF4),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = White)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Dropdown untuk memilih status laporan
                    DropdownExample(
                        options = listOf("Pending", "Diterima"),
                        selectedOption = currentFilter, // Gunakan currentFilter untuk menampilkan opsi aktif
                        onOptionSelected = { filter ->
                            wasteReportViewModel.setFilter(filter) // Set filter di ViewModel
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Tampilkan pesan error jika ada
                errorMessage.value?.let {
                    Text(text = it, color = Color.Red)
                }

                // Menampilkan laporan jika data tersedia
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(wasteReports.size) { index ->
                        WasteReportCard(
                            modifier = Modifier.fillMaxWidth(),
                            wasteReport = wasteReports[index],
                            onClick = {
                                navController.navigate("detail/${wasteReports[index].id}")
                            }
                        )
                    }
                }
            }
        }
    }
}