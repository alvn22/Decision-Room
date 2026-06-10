package id.ac.pnm.decisionroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.components.BottomNavBar
import id.ac.pnm.decisionroom.components.HeaderBar
import id.ac.pnm.decisionroom.database.AppDatabase
import id.ac.pnm.decisionroom.ui.auth.LoginScreen
import id.ac.pnm.decisionroom.ui.auth.RegisterScreen
import id.ac.pnm.decisionroom.ui.dashboard.DashboardScreen
import id.ac.pnm.decisionroom.ui.history.HistoryScreen
import id.ac.pnm.decisionroom.ui.history.HistoryViewModel
import id.ac.pnm.decisionroom.ui.profile.EditProfileScreen
import id.ac.pnm.decisionroom.ui.profile.ProfileScreen
import id.ac.pnm.decisionroom.ui.room.create.CreateRoomScreen
import id.ac.pnm.decisionroom.ui.room.join.JoinRoomScreen
import id.ac.pnm.decisionroom.ui.room.result.ResultScreen
import id.ac.pnm.decisionroom.ui.room.voting.VotingRoomScreen
import id.ac.pnm.decisionroom.ui.room.waiting.WaitingRoomScreen
import id.ac.pnm.decisionroom.viewmodel.AuthViewModel
import id.ac.pnm.decisionroom.viewmodel.ProfileViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val authViewModel: AuthViewModel = viewModel()
                    val profileViewModel: ProfileViewModel = viewModel()

                    // 1. Pastikan kamu sudah menginisialisasi database Room milikmu di MainActivity
                    val database = AppDatabase.getDatabase(context) // Sesuaikan dengan nama kelas master Room DB kalian
                    val historyDao = database.historyDao()

                    // 2. Panggil ViewModel menggunakan bantuan Factory bawaan kita
                    val historyViewModel: HistoryViewModel = viewModel(
                        factory = HistoryViewModel.Factory(historyDao)
                    )

                    val navController = rememberNavController()

                    var username by remember {
                        mutableStateOf("Guest")
                    }

                    // Membaca rute halaman yang sedang aktif saat ini secara real-time
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // 2. Mengambil Data Nama dari Firestore Otomatis Saat Berada di Dashboard
                    LaunchedEffect(currentRoute) {
                        if (currentRoute == "dashboard" && authViewModel.isUserLoggedIn()) {

                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            if (uid != null) {
                                Firebase.firestore.collection("users").document(uid)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        if (document != null && document.exists()) {
                                            val fullName = document.getString("fullName")
                                            if (!fullName.isNullOrEmpty()) {
                                                username = fullName
                                            }
                                        }
                                    }
                            }
                        }
                    }

                    // Logika apply components ke layar tertentu
                    val showBars =
                        currentRoute != null && currentRoute in listOf("dashboard", "room", "history", "profile")

                    Scaffold(
                        topBar = {
                            if (showBars) {
                                HeaderBar(
                                    onProfileClick = {
                                        // Jika belum di profile, tombol kanan akan mengantar ke profile
                                        if (currentRoute != "profile") {
                                            navController.navigate("profile")
                                        }
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            if (showBars) {
                                BottomNavBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                            modifier = Modifier.padding(innerPadding),
                            // Transisi biar bagus aja
                            enterTransition = { fadeIn(animationSpec = snap()) },
                            exitTransition = { fadeOut(animationSpec = snap()) },
                            popEnterTransition = { fadeIn(animationSpec = snap()) },
                            popExitTransition = { fadeOut(animationSpec = snap()) }
                        ) {
                            composable("login") {
                                LoginScreen(
                                    onLoginClick = { email, password ->
                                        authViewModel.login(
                                            email = email,
                                            password = password,
                                            onSuccess = {
                                                Toast.makeText(
                                                    context,
                                                    "Login Berhasil!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("dashboard") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            },
                                            onError = { errorMessage ->
                                                Toast.makeText(
                                                    context,
                                                    "Gagal: $errorMessage",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        )
                                    },
                                    onNavigateToRegister = { navController.navigate("register") }
                                )
                            }

                            composable("register") {
                                RegisterScreen(
                                    onRegisterClick = { namaLengkap, email, password ->
                                        authViewModel.register(
                                            namaLengkap = namaLengkap,
                                            email = email,
                                            password = password,
                                            onSuccess = {
                                                Toast.makeText(
                                                    context,
                                                    "Registrasi Berhasil! Selamat Datang.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("dashboard") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            },
                                            onError = { errorMessage ->
                                                Toast.makeText(
                                                    context,
                                                    errorMessage,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        )
                                    },
                                    onNavigateToLogin = { navController.navigate("login") }
                                )
                            }

                            composable("dashboard") {
                                DashboardScreen(
                                    username = username,
                                    onCreateRoomClick = {
                                        navController.navigate("create_room")
                                    },
                                    onNavigateHistory = {
                                        navController.navigate("history")
                                    }
                                )
                            }

                            composable("room") {
                                JoinRoomScreen(
                                    username = username,
                                    onCreateRoom = { navController.navigate("create_room") },
                                    onJoinSuccess = { roomId ->
                                        navController.navigate("waiting_room/$roomId")
                                    }
                                )
                            }

                            composable(
                                route = "waiting_room/{roomId}",
                                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                                WaitingRoomScreen(
                                    roomId = roomId,
                                    navigateToVoting = { navController.navigate("voting_room/$roomId") }
                                )
                            }

                            composable("create_room") {
                                CreateRoomScreen(
                                    username = username,
                                    onRoomCreated = { roomId ->
                                        navController.navigate("waiting_room/$roomId") {
                                            popUpTo("dashboard") // Mencegah user kembali ke form input saat menekan Back
                                        }
                                    }
                                )
                            }

                            composable(
                                route = "voting_room/{roomId}",
                                arguments = listOf(navArgument("roomId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                                VotingRoomScreen(
                                    roomId = roomId,
                                    navigateToResult = {
                                        navController.navigate("result_room/$roomId") {
                                            popUpTo("dashboard") { inclusive = false }
                                        }
                                    }
                                )
                            }

                            composable(
                                route = "result_room/{roomId}",
                                arguments = listOf(navArgument("roomId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                                ResultScreen(
                                    roomId = roomId
                                )
                            }

                            composable("history") {
                                HistoryScreen()
                            }

                            composable("profile") {
                                ProfileScreen(
                                    onLogoutClick = {
                                        authViewModel.logout(
                                            onSuccess = {
                                                Toast.makeText(
                                                    context,
                                                    "Berhasil Keluar Akun",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("login") {
                                                    popUpTo(0) {
                                                        inclusive = true
                                                    } // Bersihkan tumpukan halaman agar aman
                                                }
                                            }
                                        )
                                    },
                                    onNavigateToEdit = { navController.navigate("edit_profile") },
                                )
                            }

                            composable("edit_profile") {
                                val appContext = context.applicationContext

                                EditProfileScreen(
                                    onBackClick = { navController.navigate("profile") },
                                    // Menggunakan profileViewModel di halaman Edit Profile
                                    onSaveClick = { newName, newEmail, newPassword ->
                                        profileViewModel.updateProfile(
                                            newName = newName,
                                            newEmail = newEmail,
                                            newPassword = newPassword,
                                            onSuccess = { successMessage ->
                                                Toast.makeText(
                                                    appContext,
                                                    successMessage,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("profile")
                                            },
                                            onError = { errorMessage ->
                                                Toast.makeText(
                                                    appContext,
                                                    errorMessage,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            },
                                            onNoChanges = {
                                                Toast.makeText(
                                                    appContext,
                                                    "Tidak ada data perubahan yang diisi",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}