package com.praktikum.trassify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.view.CameraPreviewScreen
import com.praktikum.trassify.viewmodel.CameraViewModel
import com.praktikum.trassify.model.CameraRepository
import com.praktikum.trassify.viewmodel.CameraViewModelFactory

class MainActivity : ComponentActivity() {

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Akan diproses dalam @Composable
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menyusun UI dengan Compose
        setContent {
            val cameraRepository = CameraRepository(this)

            // Mendapatkan CameraViewModel di dalam @Composable
            val cameraViewModel: CameraViewModel = ViewModelProvider(
                this,
                CameraViewModelFactory(cameraRepository)
            ).get(CameraViewModel::class.java)
            // Cek izin kamera
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                    // Izin diberikan, beri tahu ViewModel
                    cameraViewModel.setCameraPermissionGranted(true)
                }
                else -> {
                    // Minta izin kamera jika belum diberikan
                    cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                }
            }

            // Set UI
            TrassifyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Pass CameraViewModel ke CameraPreviewScreen
                    CameraPreviewScreen(viewModel = cameraViewModel)
                }
            }
        }
    }
}
