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

    var selectedOption by mutableStateOf(-1)
        private set

    fun observeRoom(roomId: String){
        repository.observeRoom(
            roomId
        ){
            room = it
            val uid = FirebaseManager.currentUid()
            if(uid != null && it?.votes?.containsKey(uid) == true){
                selectedOption = it.votes[uid] ?: -1
            }
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
            onSuccess = {
                selectedOption = optionIndex
                onSuccess
            },
            onError = onError
        )
    }

    fun endVoting(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentRoom =
            room
        if (currentRoom == null) {
            onError("Room tidak ditemukan")
            return
        }

        val totalParticipant =
            currentRoom.participants.size

        val totalVote =
            currentRoom.votes.size

        if (
            totalVote <
            totalParticipant
        ) {
            onError(
                "Ada peserta yang belum melakukan voting"
            )
            return
        }
        repository.endVoting(roomId)
        onSuccess()
    }
}