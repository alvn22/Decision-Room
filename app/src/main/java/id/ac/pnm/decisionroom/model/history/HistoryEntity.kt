package id.ac.pnm.decisionroom.model.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voting_history")
data class HistoryEntity(
    @PrimaryKey val roomId: String,
    val roomName: String,
    val totalParticipants: Int,
    val winnerOption: String,
    val dateFinished: Long
)