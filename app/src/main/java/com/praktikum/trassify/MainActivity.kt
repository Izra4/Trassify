package com.praktikum.trassify

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.view.BottomNavBar
import com.praktikum.trassify.view.CameraPreviewScreen
import com.praktikum.trassify.view.ReportWastePage
import com.praktikum.trassify.viewmodel.BottomNavViewModel
import com.praktikum.trassify.viewmodel.CameraViewModel
import com.praktikum.trassify.viewmodel.CameraViewModelFactory
import com.praktikum.trassify.viewmodel.WasteReportViewModel
import com.praktikum.trassify.viewmodel.WasteReportViewModelFactory
import com.praktikum.trassify.viewmodel.LocationViewModel
import com.praktikum.trassify.model.CameraRepository
import com.praktikum.trassify.data.repository.WasteReportRepository

class MainActivity : ComponentActivity() {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var bottomNavViewModel: BottomNavViewModel
    private lateinit var wasteReportViewModel: WasteReportViewModel

    // Launcher untuk meminta izin kamera
    private val cameraPermissionRequest by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            cameraViewModel.updateCameraPermission(isGranted)
        }
    }

    // Launcher untuk memilih gambar dari galeri
    private val pickImageLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                cameraViewModel.setImageUri(it) // Update URI gambar di ViewModel
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi CameraViewModel
        val cameraRepository = CameraRepository(this)
        cameraViewModel = ViewModelProvider(
            this,
            CameraViewModelFactory(cameraRepository)
        )[CameraViewModel::class.java]

        // Inisialisasi BottomNavViewModel
        bottomNavViewModel = ViewModelProvider(this)[BottomNavViewModel::class.java]

        // Inisialisasi WasteReportViewModel menggunakan Factory
        val wasteReportRepository = WasteReportRepository(FirebaseDatabase.getInstance().reference) // Inisialisasi repository untuk WasteReport
        val wasteReportViewModelFactory = WasteReportViewModelFactory(wasteReportRepository)
        wasteReportViewModel = ViewModelProvider(
            this,
            wasteReportViewModelFactory
        )[WasteReportViewModel::class.java]

        val locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        // Periksa izin kamera sebelum Compose dimulai
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        } else {
            cameraViewModel.updateCameraPermission(true)
        }

        setContent {
            TrassifyApp(
                cameraViewModel = cameraViewModel,
                bottomNavViewModel = bottomNavViewModel,
                locationViewModel = locationViewModel,
                wasteReportViewModel = wasteReportViewModel,  // Pass WasteReportViewModel ke composable
                onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                onPickImage = { pickImageLauncher.launch("image/*") }
            )
        }
    }
}

@Composable
fun TrassifyApp(
    cameraViewModel: CameraViewModel,
    bottomNavViewModel: BottomNavViewModel,
    locationViewModel: LocationViewModel,
    wasteReportViewModel: WasteReportViewModel,  // Terima ViewModel sebagai parameter
    onRequestCameraPermission: () -> Unit, // Fungsi untuk meminta izin kamera
    onPickImage: () -> Unit // Fungsi untuk memilih gambar
) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    TrassifyTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                bottomBar = {
                    // Tampilkan BottomNavBar hanya jika bukan di halaman kamera
                    if (currentRoute?.destination?.route != "camera") {
                        BottomNavBar(
                            selectedIndex = bottomNavViewModel.selectedIndex.value,
                            onItemSelected = { index ->
                                bottomNavViewModel.onItemSelected(index)
                                when (index) {
                                    0 -> navController.navigate("home")
                                    1 -> navController.navigate("jadwal")
                                    2 -> navController.navigate("berita")
                                    3 -> navController.navigate("merchandise")
                                }
                            },
                            onCameraClick = {
                                navController.navigate("camera")
                            },
                            isCameraSelected = currentRoute?.destination?.route == "camera"
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            CameraPreviewScreen(
                                navController = navController,
                                viewModel = cameraViewModel,
                                onRequestCameraPermission = onRequestCameraPermission,
                                onPickImage = onPickImage
                            )
                        }
                        composable("camera") {
                            CameraPreviewScreen(
                                navController = navController,
                                viewModel = cameraViewModel,
                                onRequestCameraPermission = onRequestCameraPermission,
                                onPickImage = onPickImage
                            )
                        }
                        composable("reportWaste") {
                            ReportWastePage(
                                cameraViewModel = cameraViewModel,
                                locationViewModel = locationViewModel,
                                wasteReportViewModel = wasteReportViewModel // Pass ViewModel ke page ini
                            )
                        }
                    }
                }
            }
        }
    }
}
