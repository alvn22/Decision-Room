package id.ac.pnm.decisionroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ac.pnm.decisionroom.model.UserProfile

// Enum untuk rute halaman
enum class Screen {
    LOGIN, REGISTER, PROFILE, EDIT_PROFILE
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

                    // Cek status login
                    var currentScreen by remember {
                        mutableStateOf(
                            if (auth.currentUser != null) Screen.PROFILE else Screen.LOGIN
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
                                                    Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                                                    currentScreen = Screen.PROFILE
                                                } else {
                                                    Toast.makeText(context, "Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(context, "Email dan Password wajib diisi", Toast.LENGTH_SHORT).show()
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
                                                    db.collection("users").document(uid).set(newUserProfile)

                                                    Toast.makeText(context, "Registrasi Berhasil! Selamat Datang.", Toast.LENGTH_SHORT).show()
                                                    currentScreen = Screen.PROFILE
                                                } else {
                                                    Toast.makeText(context, "Gagal mendaftar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(context, "Semua data wajib diisi", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onNavigateToLogin = {
                                    currentScreen = Screen.LOGIN
                                }
                            )
                        }

                        Screen.PROFILE -> {
                            ProfileScreen(
                                onLogoutClick = {
                                    auth.signOut()
                                    Toast.makeText(context, "Berhasil Keluar Akun", Toast.LENGTH_SHORT).show()
                                    currentScreen = Screen.LOGIN
                                },
                                onNavigateToEdit = {
                                    currentScreen = Screen.EDIT_PROFILE
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
                                                    Toast.makeText(appContext, "Nama berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                                    currentScreen = Screen.PROFILE
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(appContext, "Gagal memperbarui nama: ${e.message}", Toast.LENGTH_SHORT).show()
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
                                                                Toast.makeText(appContext, "Email berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                                                currentScreen = Screen.PROFILE
                                                            }
                                                            .addOnFailureListener { e ->
                                                                Toast.makeText(appContext, "Auth email ganti, tapi gagal update database: ${e.message}", Toast.LENGTH_SHORT).show()
                                                            }
                                                    } else {
                                                        Toast.makeText(appContext, "Gagal ganti email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                        }

                                        // 3. UPDATE PASSWORD
                                        if (newPassword.isNotEmpty()) {
                                            anyChanges = true
                                            user?.updatePassword(newPassword)
                                                ?.addOnSuccessListener {
                                                    Toast.makeText(appContext, "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                                    currentScreen = Screen.PROFILE
                                                }
                                                ?.addOnFailureListener { e ->
                                                    Toast.makeText(appContext, "Gagal ganti password: ${e.message}", Toast.LENGTH_LONG).show()
                                                }
                                        }

                                        // Jika tombol ditekan tapi semua form kosong
                                        if (!anyChanges) {
                                            Toast.makeText(appContext, "Tidak ada data perubahan yang diisi", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // Jika ada data yang diisi, kita biarkan layar pindah setelah proses di dalam onSuccessListener selesai
                                            // (Catatan: Jika user mengisi Nama DAN Email, maka akan muncul 2 Toast berurutan. Ini normal dan bagus untuk feedback).
                                        }

                                    } else {
                                        Toast.makeText(appContext, "Sesi salah, silakan login kembali", Toast.LENGTH_SHORT).show()
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