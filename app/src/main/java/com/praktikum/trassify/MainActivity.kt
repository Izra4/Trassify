package com.praktikum.trassify

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.view.BottomNavBar
import com.praktikum.trassify.view.CameraPreviewScreen
import com.praktikum.trassify.view.ReportWastePage
import com.praktikum.trassify.viewmodel.BottomNavViewModel
import com.praktikum.trassify.viewmodel.CameraViewModel
import com.praktikum.trassify.viewmodel.CameraViewModelFactory
import com.praktikum.trassify.viewmodel.WasteReportViewModel
import com.praktikum.trassify.viewmodel.WasteReportViewModelFactory
import com.praktikum.trassify.model.CameraRepository
import com.praktikum.trassify.data.repository.WasteReportRepository
import com.praktikum.trassify.ui.screens.LoginScreen
import com.praktikum.trassify.ui.screens.DashboardScreen
import com.praktikum.trassify.ui.screens.RegisterScreen
import com.praktikum.trassify.view.WelcomeScreen
import com.praktikum.trassify.ui.viewmodel.AuthViewModel
import com.praktikum.trassify.ui.viewmodel.AuthViewModelFactory
import com.praktikum.trassify.view.WasteReportHistoryView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: AuthViewModel
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var bottomNavViewModel: BottomNavViewModel
    private lateinit var wasteReportViewModel: WasteReportViewModel

    // Launcher untuk meminta izin kamera
    private val cameraPermissionRequest by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            cameraViewModel.updateCameraPermission(isGranted)
        }
    }

    // Launcher untuk memilih gambar
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        loginViewModel = ViewModelProvider(this, AuthViewModelFactory(auth)).get(AuthViewModel::class.java)

        // Inisialisasi CameraViewModel
        val cameraRepository = CameraRepository(this)
        cameraViewModel = ViewModelProvider(
            this,
            CameraViewModelFactory(cameraRepository)
        )[CameraViewModel::class.java]

        // Inisialisasi BottomNavViewModel
        bottomNavViewModel = ViewModelProvider(this)[BottomNavViewModel::class.java]

        // Inisialisasi WasteReportViewModel menggunakan Factory
        val wasteReportRepository = WasteReportRepository(FirebaseDatabase.getInstance().reference)
        val wasteReportViewModelFactory = WasteReportViewModelFactory(wasteReportRepository)
        wasteReportViewModel = ViewModelProvider(
            this,
            wasteReportViewModelFactory
        )[WasteReportViewModel::class.java]

        setContent {
            TrassifyTheme {
                val navController = rememberNavController()
                val currentUser = loginViewModel.currentUser

                NavHost(
                    navController = navController,
                    startDestination = if (currentUser != null) "dashboard" else "welcome"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            onGoogleSignInClick = { signInWithGoogle(navController) }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            user = currentUser,
                            onSignOutClick = { signOut(navController) }
                        )
                    }
                    composable("welcome") {
                        WelcomeScreen(navController = navController)
                    }
                    composable("register") {
                        RegisterScreen(
                            navController = navController,
                            onGoogleSignInClick = { signInWithGoogle(navController) }
                        )
                    }
                    composable("home") {
                        TrassifyApp(
                            cameraViewModel = cameraViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*") }
                        )
                    }
                }
            }
        }
    }

    private fun signInWithGoogle(navController: NavController) {
        val credentialManager = CredentialManager.create(this)

        loginViewModel.signInWithGoogle(
            credentialManager = credentialManager,
            navController = navController,
            context = this
        )
    }

    private fun signOut(navController: NavController) {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@MainActivity)
            loginViewModel.signOut(credentialManager, navController)
        }
    }
}

@Composable
fun TrassifyApp(
    cameraViewModel: CameraViewModel,
    bottomNavViewModel: BottomNavViewModel,
    wasteReportViewModel: WasteReportViewModel,
    onRequestCameraPermission: () -> Unit,
    onPickImage: () -> Unit
) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            // Cek apakah current route adalah "camera"
            val isCameraSelected = currentRoute?.destination?.route == "camera"

            BottomNavBar(
                selectedIndex = bottomNavViewModel.selectedIndex.intValue,
                onItemSelected = { index ->
                    bottomNavViewModel.onItemSelected(index)
                    when (index) {
                        0 -> navController.navigate("home")
                        1 -> navController.navigate("jadwal")
                        2 -> navController.navigate("berita")
                        3 -> navController.navigate("merchandise")
                    }
                },
                isCameraSelected = isCameraSelected, // Berikan nilai untuk isCameraSelected
                onCameraClick = {
                    navController.navigate("camera") // Menavigasi ke halaman kamera
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    WasteReportHistoryView(wasteReportViewModel)
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
                    ReportWastePage(wasteReportViewModel = wasteReportViewModel)
                }
            }
        }
    }
}
