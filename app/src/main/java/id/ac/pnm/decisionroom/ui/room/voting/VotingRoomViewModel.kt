package id.ac.pnm.decisionroom.ui.room.voting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import id.ac.pnm.decisionroom.FirebaseManager
import id.ac.pnm.decisionroom.model.room.Room
import id.ac.pnm.decisionroom.repository.RoomRepository

class VotingRoomViewModel: ViewModel() {
    private val repository = RoomRepository()
    var room by mutableStateOf<Room?>(null)
        private set

    fun observeRoom(roomId: String){
        repository.observeRoom(
            roomId
        ){
            room = it
        }
    }

    fun vote(
        roomId: String,
        optionIndex: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        val uid = FirebaseManager.currentUid()

        if (uid == null){
            onError("Belum Login")
            return
        }

        repository.submitVote(
            roomId,
            uid,
            optionIndex,
            onSuccess,
            onError
        )
    }

    fun finishVoting(roomId: String){
        repository.endVoting(roomId)
    }
}