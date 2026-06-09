package id.ac.pnm.decisionroom.ui.room.waiting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.FirebaseManager
import id.ac.pnm.decisionroom.model.room.ChatMessage
import id.ac.pnm.decisionroom.model.room.Room
import id.ac.pnm.decisionroom.repository.RoomRepository

class WaitingRoomViewModel : ViewModel() {
    private val repository =
        RoomRepository()

    var room by mutableStateOf<Room?>(null)

    var messages by mutableStateOf(
        listOf<ChatMessage>()
    )
        private set

    fun observeChat(
        roomId: String
    ) {
        repository.observeChat(
            roomId
        ) {
            messages = it
        }
    }

    fun sendMessage(
        roomId: String,
        text: String
    ) {
        if (text.isBlank()) return
        val uid =
            FirebaseManager.currentUid()
                ?: return
        Firebase.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val fullName =
                    it.getString("fullName")
                        ?: "Guest"
                repository.sendMessage(
                    roomId,
                    ChatMessage(
                        senderId = uid,
                        senderName = fullName,
                        message = text,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
    }

    fun startVoting(roomId: String) {
        repository.startVoting(roomId)
    }

    fun observeRoom(
        roomId: String
    ) {

        repository.observeRoom(
            roomId
        ) {

            room = it
        }
    }
}