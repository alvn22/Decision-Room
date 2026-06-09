package id.ac.pnm.decisionroom.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.database.AppDatabase

@Composable
fun HistoryScreen() {

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
            .padding(16.dp)
    ) {
        if (historyList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada riwayat voting.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(historyList) { item ->
                    // Menampilkan data sederhana (Bisa kamu ganti dengan Card desainmu nanti)
                    Text(text = "Ruang: ${item.roomName} | Pemenang: ${item.winnerOption}")
                }
            }
        }
    }
}