package id.ac.pnm.decisionroom

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseManager {
    val auth = FirebaseAuth.getInstance()

    val db = FirebaseDatabase.getInstance().reference

    fun currentUid(): String? {
        return auth.currentUser?.uid
    }
}