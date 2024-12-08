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
import com.praktikum.trassify.model.CameraRepository

class CameraViewModel(private val cameraRepository: CameraRepository) : ViewModel() {
    val cameraPermissionGranted = mutableStateOf(false)

    // Fungsi untuk mengupdate status izin kamera
    fun setCameraPermissionGranted(granted: Boolean) {
        cameraPermissionGranted.value = granted
    }

    private val _lensFacing = mutableStateOf(CameraSelector.LENS_FACING_BACK)
    val lensFacing: State<Int> = _lensFacing

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    fun switchCamera() {
        _lensFacing.value = if (_lensFacing.value == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    suspend fun setupCamera(
        lifecycleOwner: LifecycleOwner,
        context: Context,
        preview: Preview,
        imageCapture: ImageCapture
    ) {
        val cameraProvider = cameraRepository.getCameraProvider()
        cameraProvider.unbindAll()

        val cameraxSelector = CameraSelector.Builder()
            .requireLensFacing(_lensFacing.value)
            .build()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraxSelector,
            preview,
            imageCapture
        )
    }

    fun captureImage(imageCapture: ImageCapture, context: Context) {
        val name = "CameraxImage_${System.currentTimeMillis()}.jpeg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
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
                    // Update imageUri with the captured image URI
                    _imageUri.value = outputFileResults.savedUri
                    println("Image saved: ${outputFileResults.savedUri}")
                }

                override fun onError(exception: ImageCaptureException) {
                    println("Capture failed: $exception")
                }
            }
        )
    }
}

