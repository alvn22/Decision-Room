package id.ac.pnm.decisionroom.ui.room.create

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.FirebaseManager
import id.ac.pnm.decisionroom.model.room.Participant
import id.ac.pnm.decisionroom.model.room.Room
import id.ac.pnm.decisionroom.model.room.VoteOption
import id.ac.pnm.decisionroom.repository.RoomRepository

class CreateRoomViewModel : ViewModel() {
    private val repository = RoomRepository()
    var title by mutableStateOf("")
    var options = mutableStateListOf(
        "Opsi 1",
        "Opsi 2"
    )
    private fun generateRoomId(): String {
        return (100000..999999)
            .random()
            .toString()
    }

    fun addOption() {
        options.add("")
    }

    private fun createRoomInternal(
        uid: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val roomId =
            generateRoomId()
        repository.roomExists(
            roomId
        ) { exists ->
            if (exists) {
                createRoomInternal(
                    uid,
                    onSuccess,
                    onError
                )
                return@roomExists
            }
            FirebaseManager.getCurrentUserName {
                val participant =
                    Participant(
                        name = it,
                        host = true,
                        ready = true
                    )
                val room =
                    Room(
                        roomId = roomId,
                        title = title,
                        hostId = uid,
                        participants =
                            mapOf(uid to participant),
                        options =
                            options.map {
                                VoteOption(
                                    text = it,
                                    voteCount = 0
                                )
                            }
                    )
                repository.createRoom(
                    room,
                    onSuccess = {
                        onSuccess(roomId)
                    },
                    onError = onError
                )
            }
        }
    }

    fun createRoom(
        username: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid =
            FirebaseManager.currentUid()
        if (uid == null) {
            onError("Belum Login")
            return
        }
        if (title.isBlank()) {
            onError(
                "Judul voting wajib diisi"
            )
            return
        }
        if (
            options.any {
                it.trim().isEmpty()
            }
        ) {
            onError(
                "Opsi voting wajib diisi"
            )
            return
        }
        createRoomInternal(
            uid,
            onSuccess,
            onError
        )
    }
}