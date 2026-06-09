package id.ac.pnm.decisionroom.ui.room.join

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.FirebaseManager
import id.ac.pnm.decisionroom.model.room.Participant
import id.ac.pnm.decisionroom.repository.RoomRepository

class JoinRoomViewModel : ViewModel() {
    private val repository = RoomRepository()

    fun joinRoom(
        roomId: String,
        username: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid =
            FirebaseManager.currentUid()

        if (uid == null) {
            onError("Belum Login")
            return
        }

        Firebase.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val participant =
                    Participant(
                        name =
                            it.getString("fullName")
                                ?: "Guest",
                        host = false,
                        ready = true
                    )
                repository.joinRoom(
                    roomId,
                    uid,
                    participant,
                    onSuccess,
                    onError
                )
            }

//        repository.joinRoom(
//            roomId,
//            uid,
//            participant,
//            onSuccess,
//            onError
//        )
    }
}