package com.praktikum.trassify.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
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
    private val supabaseClient: SupabaseClient = App.supabaseClient) : ViewModel() {

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
                // Menggunakan uriToByteArray dari utilitas
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

    fun fetchWasteReports(
        onSuccess: (List<WasteReport>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.getWasteReports(
            onSuccess = { wasteReports ->
                onSuccess(wasteReports) // Kirim data kembali ke UI
            },
            onError = { exception ->
                onError(exception) // Kirim error kembali ke UI
            }
        )
    }
}

