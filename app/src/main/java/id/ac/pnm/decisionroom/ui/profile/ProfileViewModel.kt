package id.ac.pnm.decisionroom.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ProfileViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun updateProfile(
        newName: String,
        newEmail: String,
        newPassword: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onNoChanges: () -> Unit
    ) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid == null) {
            onError("Sesi salah, silakan login kembali")
            return
        }

        var anyChanges = false

        // 1. UPDATE NAMA DI FIRESTORE
        if (newName.isNotEmpty()) {
            anyChanges = true
            db.collection("users").document(uid).update("fullName", newName)
                .addOnSuccessListener { onSuccess("Nama berhasil diperbarui!") }
                .addOnFailureListener { e -> onError("Gagal memperbarui nama: ${e.message}") }
        }

        // 2. UPDATE EMAIL DI FIREBASE AUTH & FIRESTORE
        if (newEmail.isNotEmpty()) {
            anyChanges = true
            user.updateEmail(newEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("users").document(uid).update("email", newEmail)
                        .addOnSuccessListener { onSuccess("Email berhasil diperbarui!") }
                        .addOnFailureListener { e -> onError("Auth email ganti, tapi gagal update database: ${e.message}") }
                } else {
                    onError("Gagal ganti email: ${task.exception?.message}")
                }
            }
        }

        // 3. UPDATE PASSWORD
        if (newPassword.isNotEmpty()) {
            anyChanges = true
            user.updatePassword(newPassword)
                .addOnSuccessListener { onSuccess("Password berhasil diperbarui!") }
                .addOnFailureListener { e -> onError("Gagal ganti password: ${e.message}") }
        }

        if (!anyChanges) {
            onNoChanges()
        }
    }
}