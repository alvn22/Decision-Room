package id.ac.pnm.decisionroom.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.HowToVote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.TextGray
import id.ac.pnm.decisionroom.database.AppDatabase
import id.ac.pnm.decisionroom.model.history.HistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    onNavigateToResult: (String) -> Unit = {}
) {

    // 1. Ambil konteks dari HP untuk membuka database
    val context = LocalContext.current

    // 2. Siapkan "biji kopi" (DAO)
    val database = AppDatabase.getDatabase(context)
    val historyDao = database.historyDao()

    // 3. Masukkan "biji kopi" ke dalam mesin (ViewModel) menggunakan Factory
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(historyDao)
    )

    // Membaca data RoomDB secara real-time
    val historyList by viewModel.localHistory.collectAsState(initial = emptyList())

    // (Opsional) Jika kamu mau Opsi 2 (Sinkronisasi dari Firestore saat layar dibuka)
    LaunchedEffect(Unit) {
        viewModel.syncHistory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background((Color(0xFFF9FAFB)))
            .padding(16.dp)
    ) {
        // 1. HEADER SECTION
        Text(
            text = "History",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Review your previous boardroom decisions and outcomes.",
            softWrap = true,
            color = TextGray,
            fontSize = 16.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        if (historyList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your voting history will be shown here")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(historyList) { item ->
                    HistoryCard(
                        history = item,
                        onClick = {
                            onNavigateToResult(item.roomId)                        }
                    )
                }
            }
        }
    }
}
@Composable
fun HistoryCard(history: HistoryEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // BAGIAN ATAS: Ikon, Judul, dan Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Kotak
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFEEF2FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.HowToVote,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5) // Warna biru/ungu khas
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Teks Judul dan Partisipan
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = history.roomName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${history.totalParticipants} Participants",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Badge Status
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "FINISHED",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GARIS PEMBATAS
            HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // BAGIAN BAWAH: Tanggal dan Tombol Detail
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatTanggalHistory(history.dateFinished),
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "VIEW RESULTS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
fun formatTanggalHistory(timeInMillis: Long): String {
    if (timeInMillis == 0L) return "-"
    // Format standar seperti "Oct 12, 2023"
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timeInMillis))
}