package com.praktikum.trassify.view

import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.praktikum.trassify.viewmodel.CameraViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun CameraPreviewScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    onPickImage: () -> Unit, // Parameter untuk memilih gambar
    onRequestCameraPermission: () -> Unit // Parameter untuk meminta izin kamera
) {
    val cameraPermissionGranted by viewModel.cameraPermissionGranted
    val lensFacing by viewModel.lensFacing
    val imageUri by viewModel.imageUri

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val imageCapture = remember { ImageCapture.Builder().build() }

    if (!cameraPermissionGranted) {
        // Jika izin kamera belum diberikan, tampilkan UI untuk meminta izin
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onRequestCameraPermission) {
                Text(text = "Grant Camera Permission")
            }
        }
    } else {
        // Jika izin kamera diberikan, tampilkan UI kamera
        LaunchedEffect(lensFacing) {
            viewModel.setupCamera(lifecycleOwner, context, preview, imageCapture)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Camera Preview
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, bottom = 75.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {
                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            preview.setSurfaceProvider(surfaceProvider)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Bottom Control Bar
            Surface(
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gallery Button
                    IconButton(
                        onClick = { onPickImage() }
                    ) {
                        Icon(
                            painter = painterResource(id = com.praktikum.trassify.R.drawable.galery_icon),
                            contentDescription = "Open Gallery",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    // Capture Button (Middle Icon)
                    IconButton(
                        onClick = {
                            viewModel.captureImage(imageCapture, context)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = com.praktikum.trassify.R.drawable.camera_capture_icon),
                            contentDescription = "Capture Image",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    // Switch Camera Button
                    IconButton(
                        onClick = { viewModel.switchCamera() }
                    ) {
                        Icon(
                            painter = painterResource(id = com.praktikum.trassify.R.drawable.camera_rotate_icon),
                            contentDescription = "Switch Camera",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        // Navigasi ke halaman report waste setelah foto diambil
        LaunchedEffect(imageUri) {
            imageUri?.let {
                navController.navigate("reportWaste")
            }
        }
    }
}
