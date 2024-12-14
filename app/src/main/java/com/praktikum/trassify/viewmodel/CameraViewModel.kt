package com.praktikum.trassify.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.praktikum.trassify.data.repository.CameraRepository

class CameraViewModel(private val cameraRepository: CameraRepository) : ViewModel() {
    // Menyimpan status izin akses kamera
    private val _cameraPermissionGranted = mutableStateOf(false)
    val cameraPermissionGranted: State<Boolean> = _cameraPermissionGranted

    // Fungsi untuk mengupdate status izin kamera
    fun updateCameraPermission(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    private val _lensFacing = mutableStateOf(CameraSelector.LENS_FACING_BACK)
    val lensFacing: State<Int> = _lensFacing

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    // Fungsi untuk mengganti kamera (depan/belakang)
    fun switchCamera() {
        _lensFacing.value = if (_lensFacing.value == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
    }

    // Fungsi untuk mengupdate URI gambar yang dipilih atau diambil
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    // Setup kamera menggunakan CameraX
    suspend fun setupCamera(
        lifecycleOwner: LifecycleOwner,
        context: Context,
        preview: Preview,
        imageCapture: ImageCapture
    ) {
        val cameraProvider = cameraRepository.getCameraProvider()
        cameraProvider.unbindAll()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(_lensFacing.value)
            .build()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }

    // Fungsi untuk menangkap gambar dari kamera
    fun captureImage(imageCapture: ImageCapture, context: Context) {
        val name = "WasteImage_${System.currentTimeMillis()}.jpeg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Trasify-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    _imageUri.value = outputFileResults.savedUri
                }

                override fun onError(exception: ImageCaptureException) {
                    println("Capture failed: ${exception.message}")
                }
            }
        )
    }
}
