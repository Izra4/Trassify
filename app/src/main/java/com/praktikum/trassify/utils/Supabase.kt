package com.praktikum.trassify.utils

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.praktikum.trassify.R
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.time.Duration.Companion.seconds

// Fungsi untuk menginisialisasi Supabase client yang dapat digunakan di luar Composable
fun createSupabaseClientInstance(supabaseUrl: String, supabaseKey: String): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(Storage) {
            transferTimeout = 90.seconds
        }
    }
}

suspend fun uploadImageToSupabase(
    supabaseClient: SupabaseClient, // Pass supabaseClient sebagai parameter
    bitmap: Bitmap,
    filename: String,
    bucket: String
): String? {
    return withContext(Dispatchers.IO) {
        try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val imageBytes = outputStream.toByteArray()

            val path = "images/$filename"
            val result = supabaseClient.storage.from(bucket).upload(path, imageBytes)
            if (result.path.isNotEmpty()) { // Pastikan path valid
                val publicUrl = supabaseClient.storage.from(bucket).publicUrl(path)
                Log.d("Supabase", "Public URL: $publicUrl")
                publicUrl
            } else {
                Log.e("Supabase", "Upload failed")
                null
            }
        } catch (e: Exception) {
            Log.e("Supabase", "Error uploading image", e)
            null
        }
    }
}

