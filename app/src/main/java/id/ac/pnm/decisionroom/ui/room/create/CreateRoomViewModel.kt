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
    var anonymousVoting by mutableStateOf(false)
    var options = mutableStateListOf(
        "Opsi 1",
        "Opsi 2"
    )

    private fun getCurrentUserName(
        onResult: (String) -> Unit
    ) {
        val uid = FirebaseManager.currentUid()
            ?: return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val name =
                    it.getString("fullName")
                        ?: "Guest"
                onResult(name)
            }
    }

    fun addOption() {
        options.add("")
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
        val roomId =
            (100000..999999)
                .random()
                .toString()
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
                    anonymousVoting = anonymousVoting,
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
//        val room =
//            Room(
//                roomId = roomId,
//                title = title,
//                hostId = uid,
//                anonymousVoting = anonymousVoting,
//                participants =
//                    mapOf(uid to participant),
//                options =
//                    options.map {
//                        VoteOption(text = it, voteCount = 0)
//                    }
//            )
//        repository.createRoom(
//            room,
//            onSuccess = {
//                onSuccess(roomId)
//            },
//            onError = onError
//        )
}