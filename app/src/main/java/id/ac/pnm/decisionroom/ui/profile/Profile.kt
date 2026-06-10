package id.ac.pnm.decisionroom.ui.profile // Sesuaikan package Anda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
// --- Tambahkan Import Firebase & Model ---
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import id.ac.pnm.decisionroom.BackgroundLight
import id.ac.pnm.decisionroom.PrimaryNavy
import id.ac.pnm.decisionroom.TextGray
import id.ac.pnm.decisionroom.database.AppDatabase
import id.ac.pnm.decisionroom.model.auth.UserProfile
import id.ac.pnm.decisionroom.viewmodel.AuthViewModel
import id.ac.pnm.decisionroom.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    // 1. Inisialisasi awal variabel state dengan nilai kosong/default
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Member") }
    var votesCast by remember { mutableStateOf(0) }
    var roomsHosted by remember { mutableStateOf(0) }

    // State untuk memunculkan loading spinner saat mengambil data dari internet
    var isLoading by remember { mutableStateOf(true) }

    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()


    val context = LocalContext.current
    val historyDao = AppDatabase.getDatabase(context).historyDao()
    val LogoutRed = Color(0xFFD32F2F)
    val LightBlueAccent = Color(0xFF8C9EFF)

    // 2. LOGIKA MENGAMBIL DATA DARI FIRESTORE
    // Blok ini berjalan otomatis sekali saat ProfileScreen pertama kali dimuat
    LaunchedEffect(Unit) {
        profileViewModel.getUserProfile(
            onSuccess = { profile ->
                fullName = profile.fullName
                email = profile.email
                role = profile.role
                votesCast = profile.votesCast
                roomsHosted = profile.roomsHosted
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )
    }

    // 3. KONDISI TAMPILAN
    if (isLoading) {
        // Tampilkan indikator putar (loading) jika data sedang dijemput dari Firebase
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryNavy)
        }
    } else {
    // Jika data sudah tiba, gambar layout Scaffold Profil Anda

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Avatar", tint = Color.LightGray, modifier = Modifier.size(80.dp))
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(PrimaryNavy, CircleShape)
                                .clickable { onNavigateToEdit() } // AKSI KLIK TOMBOL PENSIL
                                .padding(6.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- SEKARANG MENGGUNAKAN DATA DINAMIS ---
                    Text(text = fullName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = role, fontSize = 12.sp, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Account Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ACCOUNT STATUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier
                                .width(2.dp)
                                .height(30.dp)
                                .background(
                                    PrimaryNavy
                                ))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                // --- DATA DINAMIS ---
                                Text(text = "$votesCast", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                                Text("VOTES CAST", fontSize = 10.sp, color = TextGray)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier
                                .width(2.dp)
                                .height(30.dp)
                                .background(Color(0xFF26A69A)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                // --- DATA DINAMIS ---
                                Text(text = "$roomsHosted", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF26A69A))
                                Text("ROOMS HOSTED", fontSize = 10.sp, color = TextGray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

// Settings Form (Hanya Menampilkan Data)
            Text("Settings", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // --- TAMPILAN FULL NAME ---
                    Text("FULL NAME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = fullName.ifEmpty { "-" }, // Jika kosong, tampilkan strip
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- TAMPILAN EMAIL ---
                    Text("EMAIL ADDRESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = email.ifEmpty { "-" },
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- TAMPILAN PASSWORD (Hanya disensor) ---
                    Text("PASSWORD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "••••••••", // Jangan pernah menampilkan password asli ke layar
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

                Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    authViewModel.logout(
                        historyDao = historyDao,
                        onSuccess = { onLogoutClick() }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, LogoutRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LogoutRed)
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}