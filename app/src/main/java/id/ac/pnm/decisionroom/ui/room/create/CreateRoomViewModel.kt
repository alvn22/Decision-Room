package id.ac.pnm.decisionroom.ui.room.create

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
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

        val participant =
            Participant(
                name = username,
                isHost = true,
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
                    options.mapIndexed { index, text ->

                        index.toString() to
                                VoteOption(text)
                    }.toMap()
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