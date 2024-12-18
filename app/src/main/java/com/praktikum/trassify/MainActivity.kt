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
import com.praktikum.trassify.data.repository.ArticleRepository
import com.praktikum.trassify.data.repository.CameraRepository
import com.praktikum.trassify.data.repository.MerchandiseRepository
import com.praktikum.trassify.data.repository.ScheduleRepository
import com.praktikum.trassify.data.repository.UserRepository
import com.praktikum.trassify.data.repository.WasteReportRepository
import com.praktikum.trassify.ui.screens.LoginScreen
import com.praktikum.trassify.ui.screens.RegisterScreen
import com.praktikum.trassify.view.WelcomeScreen
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.view.*
import com.praktikum.trassify.viewmodel.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    private lateinit var articleRepository: ArticleRepository
    private lateinit var merchandiseRepository: MerchandiseRepository
    private lateinit var scheduleRepository: ScheduleRepository
    private lateinit var authViewModel: AuthViewModel
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var merchandiseViewModel: MerchandiseViewModel
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
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
        val database = FirebaseDatabase.getInstance().reference

        userRepository = UserRepository(database, auth)

        articleRepository = ArticleRepository(database = database)

        merchandiseRepository = MerchandiseRepository(database)

        scheduleRepository = ScheduleRepository(database)

        // Buat factory menggunakan repository yang sudah ada
        val dashboardFactory = DashboardViewModelFactory(
            userRepository = userRepository,
            articleRepository = articleRepository,
            merchandiseRepository = merchandiseRepository,
            scheduleRepository = scheduleRepository
        )

// Inisialisasi DashboardViewModel dengan factory
        dashboardViewModel = ViewModelProvider(
            this,
            dashboardFactory
        )[DashboardViewModel::class.java]

        // Inisialisasi AuthViewModel dengan UserRepository
        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(auth, userRepository)
        )[AuthViewModel::class.java]

        scheduleViewModel = ViewModelProvider(
            this,
            ScheduleViewModelFactory(scheduleRepository)
        )[ScheduleViewModel::class.java]

        merchandiseViewModel = ViewModelProvider(
            this,
            MerchandiseViewModelFactory(userRepository, merchandiseRepository)
        )[MerchandiseViewModel::class.java]

        articleViewModel = ViewModelProvider(
            this,
            ArticleViewModelFactory(articleRepository)
        )[ArticleViewModel::class.java]

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
        val wasteReportRepository = WasteReportRepository(database)
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
                        DashboardView(
                            viewModel = dashboardViewModel,
                            navController = navController,)
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
                            dashboardViewModel = dashboardViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            cameraViewModel = cameraViewModel,
                            scheduleViewModel = scheduleViewModel,
                            merchandiseViewModel = merchandiseViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            articleViewModel = articleViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*")}
                        )
                    }
                    composable("schedule") {
                        TrassifyApp(
                            navController = navController,
                            dashboardViewModel = dashboardViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            cameraViewModel = cameraViewModel,
                            scheduleViewModel = scheduleViewModel,
                            merchandiseViewModel = merchandiseViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            articleViewModel = articleViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*")}
                        )
                    }
                    composable("article") {
                        TrassifyApp(
                            navController = navController,
                            dashboardViewModel = dashboardViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            cameraViewModel = cameraViewModel,
                            scheduleViewModel = scheduleViewModel,
                            merchandiseViewModel = merchandiseViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            articleViewModel = articleViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*")}
                        )
                    }
                    composable("merchandise") {
                        TrassifyApp(
                            navController = navController,
                            dashboardViewModel = dashboardViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            cameraViewModel = cameraViewModel,
                            scheduleViewModel = scheduleViewModel,
                            merchandiseViewModel = merchandiseViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            articleViewModel = articleViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*")}
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
                        TrassifyApp(
                            navController = navController,
                            dashboardViewModel = dashboardViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            cameraViewModel = cameraViewModel,
                            scheduleViewModel = scheduleViewModel,
                            merchandiseViewModel = merchandiseViewModel,
                            wasteReportViewModel = wasteReportViewModel,
                            articleViewModel = articleViewModel,
                            onRequestCameraPermission = { cameraPermissionRequest.launch(Manifest.permission.CAMERA) },
                            onPickImage = { pickImageLauncher.launch("image/*")}
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
                    composable(
                        route = "merchandise/{merchandiseId}",
                        arguments = listOf(navArgument("merchandiseId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val merchandiseId = backStackEntry.arguments?.getString("merchandiseId")
                        // Pastikan merchandiseId tidak null sebelum menggunakan
                        if (merchandiseId != null) {
                            MerchandiseDetailScreen(
                                merchId = merchandiseId,
                                viewModel = merchandiseViewModel // Pastikan viewModel sudah terintegrasi
                            )
                        }
                    }
                    composable(
                        route = "article/{articleId}",
                        arguments = listOf(navArgument("articleId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val merchandiseId = backStackEntry.arguments?.getString("articleId")
                        // Pastikan merchandiseId tidak null sebelum menggunakan
                        if (merchandiseId != null) {
                            ArticleDetailScreen(
                                articleId = merchandiseId,
                                viewModel = articleViewModel // Pastikan viewModel sudah terintegrasi
                            )
                        }
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
    navController: NavController,
    dashboardViewModel: DashboardViewModel,
    bottomNavViewModel: BottomNavViewModel,
    cameraViewModel: CameraViewModel,
    merchandiseViewModel: MerchandiseViewModel,
    wasteReportViewModel: WasteReportViewModel,
    articleViewModel: ArticleViewModel,
    onRequestCameraPermission: () -> Unit,
    onPickImage: () -> Unit,
    scheduleViewModel: ScheduleViewModel
) {
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            // Menampilkan BottomNavBar hanya jika user sudah login
            if (currentRoute?.destination?.route != "login" && currentRoute?.destination?.route != "register"
                && currentRoute?.destination?.route != "camera") {
                BottomNavBar(
                    selectedIndex = bottomNavViewModel.selectedIndex.intValue,
                    onItemSelected = { index ->
                        bottomNavViewModel.onItemSelected(index)
                        when (index) {
                            0 -> navController.navigate("home")
                            1 -> navController.navigate("schedule")
                            2 -> navController.navigate("article")
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
            // Menampilkan konten sesuai dengan halaman yang sedang aktif
            when (currentRoute?.destination?.route) {
                "home" -> DashboardView(
                    viewModel = dashboardViewModel,
                    navController = navController)
                "schedule" -> ScheduleView(scheduleViewModel)
                "camera" -> CameraPreviewScreen(
                    navController = navController,
                    viewModel = cameraViewModel,
                    onRequestCameraPermission = onRequestCameraPermission,
                    onPickImage = onPickImage
                )
                "article" -> ArticleScreen(navController,articleViewModel)
                "merchandise" -> MerchandiseView(
                    merchandiseViewModel,
                    navController)
                "reportWasteHistory" -> WasteReportHistoryView(
                    navController = navController,
                    wasteReportViewModel = wasteReportViewModel
                )
            }
        }
    }
}

