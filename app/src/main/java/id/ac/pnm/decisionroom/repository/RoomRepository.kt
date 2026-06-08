package id.ac.pnm.decisionroom.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.ac.pnm.decisionroom.FirebaseManager
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
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "")
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