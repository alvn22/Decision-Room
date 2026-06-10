package id.ac.pnm.decisionroom.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.ac.pnm.decisionroom.database.HistoryDao
import id.ac.pnm.decisionroom.model.history.HistoryEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyDao: HistoryDao) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // 1. Aliran Data Real-time dari RoomDB ke UI Compose
    // Menggunakan stateIn agar Flow dari Room berubah menjadi StateFlow yang siap dibaca collectAsState()
    val localHistory: StateFlow<List<HistoryEntity>> = historyDao.getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Fungsi Sinkronisasi Data dari Cloud Firestore ke RoomDB
    fun syncHistory() {
        val uid = auth.currentUser?.uid ?: return

        // Mengambil data dari path: users -> {uid_user} -> voting_history
        firestore.collection("users")
            .document(uid)
            .collection("voting_history")
            .get()
            .addOnSuccessListener { snapshot ->
                // Jalankan di Coroutine Scope karena proses insert ke RoomDB adalah suspend function
                viewModelScope.launch {
                    if (snapshot != null && !snapshot.isEmpty) {
                        for (document in snapshot.documents) {
                            val roomId = document.id
                            val roomName = document.getString("roomName") ?: "Tanpa Nama"
                            val totalParticipants = document.getLong("totalParticipants")?.toInt() ?: 0
                            val winnerOption = document.getString("winnerOption") ?: "-"
                            val dateFinished = document.getLong("dateFinished") ?: 0L

                            // Bungkus ke dalam bentuk Entity RoomDB
                            val historyItem = HistoryEntity(
                                roomId = roomId,
                                roomName = roomName,
                                totalParticipants = totalParticipants,
                                winnerOption = winnerOption,
                                dateFinished = dateFinished
                            )

                            // Masukkan atau timpa data lama di local storage HP
                            historyDao.insertHistory(historyItem)
                        }
                        Log.d("SYNC_HISTORY", "Berhasil menyinkronkan ${snapshot.size()} data ke RoomDB.")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SYNC_HISTORY", "Gagal mengambil data dari Firestore: ${exception.message}")
            }
    }

    // 3. FACTORY PROVIDER
    // Berfungsi sebagai jembatan agar ViewModel bisa menerima parameter HistoryDao saat diinisialisasi
    class Factory(private val historyDao: HistoryDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                return HistoryViewModel(historyDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}