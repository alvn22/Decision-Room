package id.ac.pnm.decisionroom.ui.room.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.database.HistoryDao
import id.ac.pnm.decisionroom.model.history.HistoryEntity
import id.ac.pnm.decisionroom.model.room.Room
import id.ac.pnm.decisionroom.repository.RoomRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResultViewModel : ViewModel() {
    private val repository = RoomRepository()
    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    var room by mutableStateOf<Room?>(null)
        private set

    fun observeRoom(roomId: String) {
        repository.observeRoom(roomId) {
            room = it
        }
    }

    fun saveToHistory(
        historyDao: HistoryDao
    ) {
        val currentRoom = room ?: return

        val uid = auth.currentUser?.uid ?: return

        val voteCountMap =
            mutableMapOf<Int, Int>()

        currentRoom.votes.values.forEach {
            voteCountMap[it] =
                (voteCountMap[it] ?: 0) + 1
        }

        val winnerIndex =
            voteCountMap.maxByOrNull {
                it.value
            }?.key ?: -1

        val winner =
            currentRoom.options
                .getOrNull(winnerIndex)
                ?.text ?: "-"

        val currentTime = System.currentTimeMillis()

        val historyMap = hashMapOf(
            "roomName" to currentRoom.title,
            "totalParticipants" to currentRoom.participants.size,
            "winnerOption" to winner,
            "dateFinished" to currentTime
        )

        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(uid)
                    .collection("voting_history")
                    .document(currentRoom.roomId)
                    .set(historyMap)
                    .await()

                historyDao.insertHistory(
                    HistoryEntity(
                        roomId = currentRoom.roomId,
                        roomName = currentRoom.title,
                        totalParticipants = currentRoom.participants.size,
                        winnerOption = winner,
                        dateFinished = currentTime
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}