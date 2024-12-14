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
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.praktikum.trassify.data.repository.CameraRepository
import com.praktikum.trassify.data.repository.UserRepository
import com.praktikum.trassify.data.repository.WasteReportRepository
import com.praktikum.trassify.data.model.Article
import com.praktikum.trassify.data.model.Merchandise
import com.praktikum.trassify.data.model.Schedule
import com.praktikum.trassify.ui.screens.DashboardScreen
import com.praktikum.trassify.ui.screens.LoginScreen
import com.praktikum.trassify.ui.screens.RegisterScreen
import com.praktikum.trassify.view.WelcomeScreen
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.utils.formatTimestamp
import com.praktikum.trassify.view.*
import com.praktikum.trassify.viewmodel.*
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

        // Inisialisasi WasteReportViewModel
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

        // Bagian Seeding Data Article dan Merchandise
        seedScheduleData()

        setContent {
            TrassifyTheme {
                val navController = rememberNavController()
                val currentUser by authViewModel.currentUser.collectAsState()

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
                        CameraPreviewScreen(
                            navController = navController,
                            viewModel = cameraViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*") }
                        )
                    }
                    composable("reportWaste") {
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

    /**
     * Fungsi untuk melakukan seeding data articles dan merchandise
     * ke Firebase Realtime Database.
     */
    private fun seedScheduleData() {
        val db = FirebaseDatabase.getInstance().reference
        val timestamp = formatTimestamp(System.currentTimeMillis())

        val schedules = listOf(
            Schedule(
                id = "schedule1",
                village = "Dinoyo",
                subdistrict = "Lowokwaru",
                timeStamp = timestamp
            ),
            Schedule(
                id = "schedule2",
                village = "Sukun",
                subdistrict = "Sukun",
                timeStamp = timestamp
            ),
            Schedule(
                id = "schedule3",
                village = "Bunulrejo",
                subdistrict = "Blimbing",
                timeStamp = timestamp
            ),
            Schedule(
                id = "schedule4",
                village = "Kauman",
                subdistrict = "Klojen",
                timeStamp = timestamp
            ),
            Schedule(
                id = "schedule5",
                village = "Tlogowaru",
                subdistrict = "Kedungkandang",
                timeStamp = timestamp
            )
        )

        for (schedule in schedules) {
            db.child("schedules").child(schedule.id).setValue(schedule)
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
    navController: NavController,
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
