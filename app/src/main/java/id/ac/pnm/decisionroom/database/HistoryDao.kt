package id.ac.pnm.decisionroom.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.pnm.decisionroom.model.history.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    // Ambil semua data riwayat, bungkus pakai Flow agar UI otomatis terupdate jika ada data baru
    @Query("SELECT * FROM voting_history ORDER BY dateFinished DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Query("DELETE FROM voting_history") // 🔑 Sesuaikan dengan nama tabelmu di @Entity
    suspend fun clearAllHistory()
}