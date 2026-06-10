package id.ac.pnm.decisionroom.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.ac.pnm.decisionroom.FirebaseManager
import id.ac.pnm.decisionroom.model.room.ChatMessage
import id.ac.pnm.decisionroom.model.room.Participant
import id.ac.pnm.decisionroom.model.room.Room

class RoomRepository {
    private val db = FirebaseManager.db

    fun createRoom(
        room: Room,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        db.child("rooms")
            .child(room.roomId)
            .setValue(room)
            .addOnSuccessListener {
                Log.d("ROOM", "SUCCESS")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("ROOM", it.message ?: "")
                onError(it.message ?: "")
            }
    }

    fun roomExists(
        roomId: String,
        onResult: (Boolean) -> Unit
    ) {
        db.child("rooms")
            .child(roomId)
            .get()
            .addOnSuccessListener {
                onResult(
                    it.exists()
                )
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getRoom(
        roomId: String,
        onSuccess: (Room?) -> Unit,
        onError: (String) -> Unit
    ) {
        db.child("rooms")
            .child(roomId)
            .get()
            .addOnSuccessListener { snapshot ->
                val room =
                    snapshot.getValue(Room::class.java)
                onSuccess(room)
            }
            .addOnFailureListener {
                onError(
                    it.message ?: "Gagal mengambil room"
                )
            }
    }

    fun joinRoom(
        roomId: String,
        uid: String,
        participant: Participant,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.child("rooms")
            .child(roomId)
            .child("participants")
            .child(uid)
            .setValue(participant)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "")
            }
    }

    fun observeRoom(
        roomId: String,
        onUpdate: (Room?) -> Unit
    ) {
        db.child("rooms")
            .child(roomId)
            .addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        onUpdate(snapshot.getValue(Room::class.java))
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }

                }
            )
    }

    fun sendMessage(
        roomId: String,
        message: ChatMessage
    ) {
        db.child("rooms")
            .child(roomId)
            .child("chat")
            .push()
            .setValue(message)
    }

    fun observeChat(
        roomId: String,
        onUpdate: (List<ChatMessage>) -> Unit
    ) {
        db.child("rooms")
            .child(roomId)
            .child("chat")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {
                        val messages =
                            mutableListOf<ChatMessage>()
                        snapshot.children.forEach {
                            val msg =
                                it.getValue(
                                    ChatMessage::class.java
                                )
                            if (msg != null)
                                messages.add(msg)
                        }
                        onUpdate(messages)
                    }
                    override fun onCancelled(
                        error: DatabaseError
                    ) {}
                }
            )
    }

    fun submitVote(
        roomId: String,
        uid: String,
        optionIndex: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.child("rooms")
            .child(roomId)
            .child("votes")
            .child(uid)
            .setValue(optionIndex)
            .addOnSuccessListener {

                onSuccess()
            }
            .addOnFailureListener {
                onError(
                    it.message ?: "Vote gagal"
                )
            }
    }

    fun endVoting(
        roomId: String
    ) {
        db.child("rooms")
            .child(roomId)
            .child("status")
            .setValue("finished")
    }

    fun startVoting(
        roomId: String
    ){
        db.child("rooms")
            .child(roomId)
            .child("status")
            .setValue("voting")
    }
}