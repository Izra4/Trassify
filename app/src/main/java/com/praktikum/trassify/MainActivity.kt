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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.praktikum.trassify.data.repository.UserRepository
import com.praktikum.trassify.ui.screens.LoginScreen
import com.praktikum.trassify.ui.screens.DashboardScreen
import com.praktikum.trassify.ui.screens.RegisterScreen
import com.praktikum.trassify.view.WasteReportHistoryDetailPage
import com.praktikum.trassify.view.WelcomeScreen
import com.praktikum.trassify.viewmodel.AuthViewModel
import com.praktikum.trassify.viewmodel.AuthViewModelFactory
import com.praktikum.trassify.view.WasteReportHistoryView
import com.praktikum.trassify.viewmodel.LocationViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    private lateinit var authViewModel: AuthViewModel
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var bottomNavViewModel: BottomNavViewModel
    private lateinit var wasteReportViewModel: WasteReportViewModel
    private val credentialManager by lazy { CredentialManager.create(this) }

    // Pindahkan deklarasi launcher ke dalam onCreate()
    private lateinit var cameraPermissionRequest: ActivityResultLauncher<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi Firebase dan UserRepository
        auth = FirebaseAuth.getInstance()
        userRepository = UserRepository(FirebaseDatabase.getInstance(), auth)

        // Inisialisasi AuthViewModel dengan UserRepository
        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(auth, userRepository)
        )[AuthViewModel::class.java]

        // Inisialisasi CameraViewModel
        val cameraRepository = CameraRepository(this)
        cameraViewModel = ViewModelProvider(
            this,
            CameraViewModelFactory(cameraRepository)
        )[CameraViewModel::class.java]

        // Inisialisasi LocationViewModel
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]


        // Inisialisasi BottomNavViewModel
        bottomNavViewModel = ViewModelProvider(this)[BottomNavViewModel::class.java]

        // Inisialisasi WasteReportViewModel menggunakan Factory
        val wasteReportRepository = WasteReportRepository(FirebaseDatabase.getInstance().reference)
        val wasteReportViewModelFactory = WasteReportViewModelFactory(wasteReportRepository)
        wasteReportViewModel = ViewModelProvider(
            this,
            wasteReportViewModelFactory
        )[WasteReportViewModel::class.java]

        // Inisialisasi launcher
        cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            cameraViewModel.updateCameraPermission(isGranted)
        }
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Handle picked image
        }

        setContent {
            TrassifyTheme {
                val navController = rememberNavController()
                val currentUser by authViewModel.currentUser.collectAsState()

                // Main NavHost untuk seluruh aplikasi
                NavHost(
                    navController = navController,
                    startDestination = if (currentUser != null) "home" else "welcome"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            viewModel = authViewModel,
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
                        // Tampilan home dengan Bottom Navigation
                        TrassifyApp(
                            navController = navController,
                            cameraViewModel = cameraViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*") }
                        )
                    }
                    composable("camera") {
                        // Halaman kamera
                        CameraPreviewScreen(
                            navController = navController,
                            viewModel = cameraViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*") }
                        )
                    }
                    composable("reportWaste") {
                        // Halaman untuk laporan sampah
                        ReportWastePage(
                            wasteReportViewModel = wasteReportViewModel,
                            locationViewModel = locationViewModel,
                            cameraViewModel = cameraViewModel,
                            navController = navController,
                        )
                    }
                    composable("reportWasteHistory") {
                        WasteReportHistoryView(
                            navController = navController,
                            wasteReportViewModel = wasteReportViewModel
                        )
                    }
                    composable(
                        route = "detail/{reportId}",
                        arguments = listOf(navArgument("reportId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val reportId = backStackEntry.arguments?.getString("reportId") ?: return@composable
                        WasteReportHistoryDetailPage(
                            reportId = reportId,
                            wasteReportViewModel = wasteReportViewModel
                        )
                    }
                }
            }
        }
    }

    private fun signInWithGoogle(navController: NavController) {
        authViewModel.signInWithGoogle(
            credentialManager = credentialManager,
            navController = navController,
            context = this
        )
    }

    private fun signOut(navController: NavController) {
        lifecycleScope.launch {
            authViewModel.signOut(credentialManager, navController)
        }
    }
}

@Composable
fun TrassifyApp(
    navController: NavController,  // Gunakan NavController yang sama
    cameraViewModel: CameraViewModel,
    bottomNavViewModel: BottomNavViewModel,
    wasteReportViewModel: WasteReportViewModel,
    onRequestCameraPermission: () -> Unit,
    onPickImage: () -> Unit
) {
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
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
                        4 -> navController.navigate("reportWasteHistory")
                        5 -> navController.navigate("reportWaste")
                    }
                },
                isCameraSelected = isCameraSelected,
                onCameraClick = {
                    navController.navigate("camera")  // Navigasi ke halaman kamera
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Tampilan konten halaman home
            WasteReportHistoryView(navController, wasteReportViewModel)
        }
    }
}
