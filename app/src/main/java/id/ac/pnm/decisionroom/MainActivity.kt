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

                    var selectedRoomId by remember {
                        mutableStateOf("")
                    }

                    var username by remember {
                        mutableStateOf("Guest")
                    }

                    // Cek status login
                    var currentScreen by remember {
                        mutableStateOf(
                            if (auth.currentUser != null) Screen.DASHBOARD else Screen.LOGIN
                        )
                    }

                    // Logika Navigasi Antar Halaman
                    when (currentScreen) {

                        Screen.LOGIN -> {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    if (email.isNotEmpty() && password.isNotEmpty()) {
                                        auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(this) { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Login Berhasil!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    currentScreen = Screen.DASHBOARD
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Gagal: ${task.exception?.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Email dan Password wajib diisi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onNavigateToRegister = {
                                    currentScreen = Screen.REGISTER
                                }
                            )
                        }

                        Screen.REGISTER -> {
                            RegisterScreen(
                                onRegisterClick = { namaLengkap, email, password ->
                                    if (email.isNotEmpty() && password.isNotEmpty() && namaLengkap.isNotEmpty()) {
                                        auth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(this) { task ->
                                                if (task.isSuccessful) {
                                                    val uid = auth.currentUser?.uid ?: ""
                                                    val newUserProfile = UserProfile(
                                                        uid = uid,
                                                        fullName = namaLengkap,
                                                        email = email,
                                                        role = "Member",
                                                        votesCast = 0,
                                                        roomsHosted = 0,
                                                        emailNotifications = true
                                                    )

                                                    val db = Firebase.firestore
                                                    db.collection("users").document(uid)
                                                        .set(newUserProfile)

                                                    Toast.makeText(
                                                        context,
                                                        "Registrasi Berhasil! Selamat Datang.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    currentScreen = Screen.DASHBOARD
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Gagal mendaftar: ${task.exception?.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Semua data wajib diisi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onNavigateToLogin = {
                                    currentScreen = Screen.LOGIN
                                }
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
                                    auth.signOut()
                                    Toast.makeText(
                                        context,
                                        "Berhasil Keluar Akun",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    currentScreen = Screen.LOGIN
                                },
                                onNavigateToEdit = {
                                    currentScreen = Screen.EDIT_PROFILE
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

                        Screen.EDIT_PROFILE -> {
                            // Tangkap context level aplikasi agar Toast "kebal" dari perpindahan layar
                            val appContext = context.applicationContext

                            EditProfileScreen(
                                onBackClick = {
                                    currentScreen = Screen.PROFILE
                                },
                                // Menerima 3 parameter sesuai penyesuaian Anda
                                onSaveClick = { newName, newEmail, newPassword ->
                                    val uid = auth.currentUser?.uid
                                    val user = auth.currentUser

                                    if (uid != null) {
                                        val db = Firebase.firestore
                                        var anyChanges = false

                                        // 1. UPDATE NAMA DI FIRESTORE
                                        if (newName.isNotEmpty()) {
                                            anyChanges = true
                                            db.collection("users").document(uid)
                                                .update("fullName", newName)
                                                .addOnSuccessListener {
                                                    // Gunakan appContext di sini
                                                    Toast.makeText(
                                                        appContext,
                                                        "Nama berhasil diperbarui!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    currentScreen = Screen.PROFILE
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        appContext,
                                                        "Gagal memperbarui nama: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }

                                        // 2. UPDATE EMAIL DI FIREBASE AUTH & FIRESTORE
                                        if (newEmail.isNotEmpty()) {
                                            anyChanges = true
                                            user?.updateEmail(newEmail)
                                                ?.addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        db.collection("users").document(uid)
                                                            .update("email", newEmail)
                                                            .addOnSuccessListener {
                                                                Toast.makeText(
                                                                    appContext,
                                                                    "Email berhasil diperbarui!",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                currentScreen = Screen.PROFILE
                                                            }
                                                            .addOnFailureListener { e ->
                                                                Toast.makeText(
                                                                    appContext,
                                                                    "Auth email ganti, tapi gagal update database: ${e.message}",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                    } else {
                                                        Toast.makeText(
                                                            appContext,
                                                            "Gagal ganti email: ${task.exception?.message}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                        }

                                        // 3. UPDATE PASSWORD
                                        if (newPassword.isNotEmpty()) {
                                            anyChanges = true
                                            user?.updatePassword(newPassword)
                                                ?.addOnSuccessListener {
                                                    Toast.makeText(
                                                        appContext,
                                                        "Password berhasil diperbarui!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    currentScreen = Screen.PROFILE
                                                }
                                                ?.addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        appContext,
                                                        "Gagal ganti password: ${e.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        }

                                        // Jika tombol ditekan tapi semua form kosong
                                        if (!anyChanges) {
                                            Toast.makeText(
                                                appContext,
                                                "Tidak ada data perubahan yang diisi",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // Jika ada data yang diisi, kita biarkan layar pindah setelah proses di dalam onSuccessListener selesai
                                            // (Catatan: Jika user mengisi Nama DAN Email, maka akan muncul 2 Toast berurutan. Ini normal dan bagus untuk feedback).
                                        }

                                    } else {
                                        Toast.makeText(
                                            appContext,
                                            "Sesi salah, silakan login kembali",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}