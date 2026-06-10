package id.ac.pnm.decisionroom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.pnm.decisionroom.model.history.HistoryEntity

// Daftarkan semua Entity (Tabel) yang ada di sini
@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Daftarkan semua DAO yang ada di sini
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Fungsi Singleton untuk memastikan database hanya dibuat 1 kali (hemat memori)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "decision_room_db" // Nama file database di dalam memori HP
                )
                    .fallbackToDestructiveMigration() // Jika struktur tabel diubah, hapus data lama tanpa error
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}