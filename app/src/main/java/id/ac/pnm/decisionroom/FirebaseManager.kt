package id.ac.pnm.decisionroom

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore

object FirebaseManager {
    val auth = FirebaseAuth.getInstance()

    val db = FirebaseDatabase.getInstance().reference

    fun currentUid(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUserName(
        onResult: (String) -> Unit
    ) {

        val uid = currentUid()
            ?: return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                onResult(
                    it.getString("fullName")
                        ?: "Guest"
                )
            }
    }
}