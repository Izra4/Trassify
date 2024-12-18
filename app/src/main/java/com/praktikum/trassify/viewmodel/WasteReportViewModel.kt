package com.praktikum.trassify.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.trassify.App
import com.praktikum.trassify.data.model.WasteReport
import com.praktikum.trassify.data.repository.WasteReportRepository
import com.praktikum.trassify.utils.uploadImageToSupabase
import com.praktikum.trassify.utils.uriToByteArray
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WasteReportViewModel(
    private val repository: WasteReportRepository,
    private val supabaseClient: SupabaseClient = App.supabaseClient
) : ViewModel() {

    // State untuk menyimpan laporan terfilter
    private val _filteredWasteReports = mutableStateOf<List<WasteReport>>(emptyList())
    val filteredWasteReports: State<List<WasteReport>> get() = _filteredWasteReports

    // State untuk menyimpan filter status
    private val _currentFilter = mutableStateOf("Pending") // Default filter
    val currentFilter: State<String> get() = _currentFilter

    // Semua laporan yang diambil dari repository
    private val allWasteReports = mutableListOf<WasteReport>()

    fun saveWasteReport(
        wasteReport: WasteReport,
        imageUri: Uri,
        bucket: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val imageBytes = uriToByteArray(context, imageUri)
                if (imageBytes == null) {
                    onError(Exception("Gagal mengonversi gambar"))
                    return@launch
                }

                // Upload ke Supabase
                val fileName = "waste_${System.currentTimeMillis()}.jpg"
                val publicUrl = withContext(Dispatchers.IO) {
                    uploadImageToSupabase(
                        supabaseClient = supabaseClient,
                        bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size),
                        filename = fileName,
                        bucket = bucket
                    )
                }
                if (publicUrl == null) {
                    onError(Exception("Gagal mengunggah gambar ke Supabase"))
                    return@launch
                }

                val wasteReportWithPhoto = wasteReport.copy(photoReport = publicUrl)
                repository.saveWasteReport(
                    wasteReport = wasteReportWithPhoto,
                    onSuccess = onSuccess,
                    onError = onError
                )
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun fetchWasteReportDetail(
        reportId: String,
        onSuccess: (WasteReport) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.getWasteReportById(
                reportId = reportId,
                onSuccess = { wasteReport ->
                    onSuccess(wasteReport)
                },
                onError = { exception ->
                    onError(exception)
                }
            )
        }
    }

    fun fetchWasteReports(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.getWasteReportsByUserId(
                userId = userId,
                onSuccess = { reports ->
                    allWasteReports.clear()
                    allWasteReports.addAll(reports)
                    filterReports()
                    onSuccess()
                },
                onError = { exception ->
                    onError(exception)
                }
            )
        }
    }

    fun setFilter(filter: String) {
        _currentFilter.value = filter
        filterReports()
    }

    private fun filterReports() {
        val filtered = allWasteReports.filter { it.status == _currentFilter.value }
        _filteredWasteReports.value = filtered
    }
}


