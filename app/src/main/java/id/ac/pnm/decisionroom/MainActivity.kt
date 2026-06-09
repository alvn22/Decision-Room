package id.ac.pnm.decisionroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.model.auth.UserProfile
import id.ac.pnm.decisionroom.ui.auth.LoginScreen
import id.ac.pnm.decisionroom.ui.auth.RegisterScreen
import id.ac.pnm.decisionroom.ui.dashboard.DashboardScreen
import id.ac.pnm.decisionroom.ui.dashboard.HistoryScreen
import id.ac.pnm.decisionroom.ui.profile.EditProfileScreen
import id.ac.pnm.decisionroom.ui.profile.ProfileScreen
import id.ac.pnm.decisionroom.ui.room.create.CreateRoomScreen
import id.ac.pnm.decisionroom.ui.room.join.JoinRoomScreen
import id.ac.pnm.decisionroom.ui.room.result.ResultScreen
import id.ac.pnm.decisionroom.ui.room.voting.VotingRoomScreen
import id.ac.pnm.decisionroom.ui.room.waiting.WaitingRoomScreen
import id.ac.pnm.decisionroom.viewmodel.AuthViewModel
import id.ac.pnm.decisionroom.viewmodel.ProfileViewModel

// Enum untuk rute halaman
enum class Screen {
    LOGIN, REGISTER, DASHBOARD, ROOM, CREATE_ROOM, WAITING_ROOM, VOTING_ROOM, RESULT_ROOM, HISTORY, PROFILE, EDIT_PROFILE
}

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val authViewModel: AuthViewModel = viewModel()
                    val profileViewModel: ProfileViewModel = viewModel()

                    var selectedRoomId by remember {
                        mutableStateOf("")
                    }

                    var username by remember {
                        mutableStateOf("Guest")
                    }

                    var currentScreen by remember {
                        mutableStateOf(
                            if (authViewModel.isUserLoggedIn()) Screen.DASHBOARD else Screen.LOGIN
                        )
                    }

                    // Logika Navigasi Antar Halaman
                    when (currentScreen) {

                        Screen.LOGIN -> {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    authViewModel.login(
                                        email = email,
                                        password = password,
                                        onSuccess = {
                                            Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                                            currentScreen = Screen.DASHBOARD
                                        },
                                        onError = { errorMessage ->
                                            Toast.makeText(context, "Gagal: $errorMessage", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                onNavigateToRegister = { currentScreen = Screen.REGISTER }
                            )
                        }

                        Screen.REGISTER -> {
                            RegisterScreen(
                                onRegisterClick = { namaLengkap, email, password ->
                                    authViewModel.register(
                                        namaLengkap = namaLengkap,
                                        email = email,
                                        password = password,
                                        onSuccess = {
                                            Toast.makeText(context, "Registrasi Berhasil! Selamat Datang.", Toast.LENGTH_SHORT).show()
                                            currentScreen = Screen.DASHBOARD
                                        },
                                        onError = { errorMessage ->
                                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                onNavigateToLogin = { currentScreen = Screen.LOGIN }
                            )
                        }

                        Screen.DASHBOARD -> {
                            DashboardScreen(
                                username = username,
                                onCreateRoomClick = {
                                    currentScreen = Screen.CREATE_ROOM
                                },
                                onNavigateHome = {
                                    currentScreen = Screen.DASHBOARD
                                },
                                onNavigateRoom = {
                                    currentScreen = Screen.ROOM
                                },
                                onNavigateHistory = {
                                    currentScreen = Screen.HISTORY
                                },
                                onNavigateProfile = {
                                    currentScreen = Screen.PROFILE
                                }
                            )
                        }

                        Screen.ROOM -> {
                            JoinRoomScreen(

                                username = username,

                                onCreateRoom = {

                                    currentScreen =
                                        Screen.CREATE_ROOM
                                },

                                onJoinSuccess = {

                                    selectedRoomId = it

                                    currentScreen  =
                                        Screen.WAITING_ROOM
                                },

                                onNavigateHome = {
                                    currentScreen = Screen.DASHBOARD
                                },
                                onNavigateRoom = {
                                    currentScreen = Screen.ROOM
                                },
                                onNavigateHistory = {
                                    currentScreen = Screen.HISTORY
                                },
                                onNavigateProfile = {
                                    currentScreen = Screen.PROFILE
                                }
                            )
                        }

                        Screen.WAITING_ROOM -> {
                            WaitingRoomScreen(
                                roomId = selectedRoomId,
                                navigateToVoting = {
                                    currentScreen = Screen.VOTING_ROOM
                                }
                            )
                        }

                        Screen.CREATE_ROOM -> {
                            CreateRoomScreen(

                                username = username,

                                onRoomCreated = {

                                    selectedRoomId = it

                                    currentScreen =
                                        Screen.WAITING_ROOM
                                }
                            )
                        }

                        Screen.VOTING_ROOM -> {
                            VotingRoomScreen(
                                roomId = selectedRoomId,
                                navigateToResult = {
                                    currentScreen =
                                        Screen.RESULT_ROOM
                                }
                            )
                        }

                        Screen.RESULT_ROOM -> {
                            ResultScreen(
                                roomId = selectedRoomId
                            )
                        }

                        Screen.HISTORY -> {
                            HistoryScreen(
                                onNavigateHome = {
                                    currentScreen = Screen.DASHBOARD
                                },
                                onNavigateRoom = {
                                    currentScreen = Screen.ROOM
                                },
                                onNavigateHistory = {
                                    currentScreen = Screen.HISTORY
                                },
                                onNavigateProfile = {
                                    currentScreen = Screen.PROFILE
                                }
                            )
                        }

                        Screen.PROFILE -> {
                            ProfileScreen(
                                onLogoutClick = {
                                    authViewModel.logout(
                                        onSuccess = {
                                            Toast.makeText(context, "Berhasil Keluar Akun", Toast.LENGTH_SHORT).show()
                                            currentScreen = Screen.LOGIN
                                        }
                                    )
                                },
                                onNavigateToEdit = { currentScreen = Screen.EDIT_PROFILE },
                                onNavigateHome = { currentScreen = Screen.DASHBOARD },
                                onNavigateRoom = { currentScreen = Screen.ROOM },
                                onNavigateHistory = { currentScreen = Screen.HISTORY },
                                onNavigateProfile = { currentScreen = Screen.PROFILE }
                            )
                        }

                        Screen.EDIT_PROFILE -> {
                            val appContext = context.applicationContext

                            EditProfileScreen(
                                onBackClick = { currentScreen = Screen.PROFILE },
                                // Menggunakan profileViewModel di halaman Edit Profile
                                onSaveClick = { newName, newEmail, newPassword ->
                                    profileViewModel.updateProfile(
                                        newName = newName,
                                        newEmail = newEmail,
                                        newPassword = newPassword,
                                        onSuccess = { successMessage ->
                                            Toast.makeText(appContext, successMessage, Toast.LENGTH_SHORT).show()
                                            currentScreen = Screen.PROFILE
                                        },
                                        onError = { errorMessage ->
                                            Toast.makeText(appContext, errorMessage, Toast.LENGTH_LONG).show()
                                        },
                                        onNoChanges = {
                                            Toast.makeText(appContext, "Tidak ada data perubahan yang diisi", Toast.LENGTH_SHORT).show()
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