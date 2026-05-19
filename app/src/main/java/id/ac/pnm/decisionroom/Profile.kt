package id.ac.pnm.decisionroom // Sesuaikan package Anda

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- Tambahkan Import Firebase & Model ---
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ac.pnm.decisionroom.model.UserProfile

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
    var emailNotifications by remember { mutableStateOf(true) }

    var currentPassword by remember { mutableStateOf("********") }
    var newPassword by remember { mutableStateOf("") }

    // State untuk memunculkan loading spinner saat mengambil data dari internet
    var isLoading by remember { mutableStateOf(true) }

    val auth = Firebase.auth
    val db = Firebase.firestore
    val LogoutRed = Color(0xFFD32F2F)
    val LightBlueAccent = Color(0xFF8C9EFF)

    // 2. LOGIKA MENGAMBIL DATA DARI FIRESTORE
    // Blok ini berjalan otomatis sekali saat ProfileScreen pertama kali dimuat
    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Mengonversi dokumen Firestore kembali menjadi objek Data Class UserProfile
                        val profile = document.toObject(UserProfile::class.java)
                        if (profile != null) {
                            // Masukkan data dari Firebase ke dalam variabel state UI
                            fullName = profile.fullName
                            email = profile.email
                            role = profile.role
                            votesCast = profile.votesCast
                            roomsHosted = profile.roomsHosted
                            emailNotifications = profile.emailNotifications
                        }
                    }
                    isLoading = false // Hentikan loading setelah data sukses diambil
                }
                .addOnFailureListener {
                    isLoading = false // Hentikan loading jika gagal (misal tidak ada internet)
                }
        } else {
            isLoading = false
        }
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
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryNavy)
                    Text(text = "Decision Room", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                    Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile", tint = PrimaryNavy)
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                        label = { Text("HOME", fontSize = 10.sp) },
                        selected = false,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.MeetingRoom, contentDescription = "Room") },
                        label = { Text("ROOM", fontSize = 10.sp) },
                        selected = false,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.History, contentDescription = "History") },
                        label = { Text("HISTORY", fontSize = 10.sp) },
                        selected = false,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                        label = { Text("PROFILE", fontSize = 10.sp) },
                        selected = true,
                        onClick = {},
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryNavy,
                            indicatorColor = LightBlueAccent.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLight)
                    .padding(innerPadding)
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
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
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
                                Box(modifier = Modifier.width(2.dp).height(30.dp).background(PrimaryNavy))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    // --- DATA DINAMIS ---
                                    Text(text = "$votesCast", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                                    Text("VOTES CAST", fontSize = 10.sp, color = TextGray)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.width(2.dp).height(30.dp).background(Color(0xFF26A69A)))
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

                // Notifications Toggle Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Notifications, contentDescription = null, tint = PrimaryNavy)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Email Notifications", fontSize = 14.sp, color = Color.Black)
                                Text("Receive live room results", fontSize = 10.sp, color = TextGray)
                            }
                        }
                        Switch(checked = emailNotifications, onCheckedChange = { emailNotifications = it }, colors = SwitchDefaults.colors(checkedTrackColor = LightBlueAccent))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
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
}