package id.ac.pnm.decisionroom.ui.room.waiting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import id.ac.pnm.decisionroom.model.room.Room
import id.ac.pnm.decisionroom.repository.RoomRepository

class WaitingRoomViewModel : ViewModel() {
    private val repository =
        RoomRepository()

    var room by mutableStateOf<Room?>(null)

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