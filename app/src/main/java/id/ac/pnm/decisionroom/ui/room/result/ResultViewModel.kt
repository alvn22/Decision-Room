package id.ac.pnm.decisionroom.ui.room.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.pnm.decisionroom.database.HistoryDao
import id.ac.pnm.decisionroom.model.history.HistoryEntity
import id.ac.pnm.decisionroom.model.room.Room
import id.ac.pnm.decisionroom.repository.RoomRepository
import kotlinx.coroutines.launch

class ResultViewModel: ViewModel() {
    private val repository = RoomRepository()

    var room by mutableStateOf<Room?>(null)
        private set

    fun observeRoom(roomId: String){
        repository.observeRoom(roomId){
            room = it
        }
    }

    fun saveToHistory(
        historyDao: HistoryDao
    ) {
        val currentRoom = room ?: return

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

        viewModelScope.launch {
            historyDao.insertHistory(
                HistoryEntity(
                    roomId = currentRoom.roomId,
                    roomName = currentRoom.title,
                    totalParticipants =
                        currentRoom.participants.size,
                    winnerOption = winner,
                    dateFinished =
                        System.currentTimeMillis()
                )
            )
        }
    }
}