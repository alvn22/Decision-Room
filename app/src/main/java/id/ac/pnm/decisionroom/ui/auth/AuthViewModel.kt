package id.ac.pnm.decisionroom.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.model.auth.UserProfile

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onError("Email dan Password wajib diisi")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Terjadi kesalahan yang tidak diketahui")
                }
            }
    }

    fun register(
        namaLengkap: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty() || namaLengkap.isEmpty()) {
            onError("Semua data wajib diisi")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val newUserProfile = UserProfile(
                        uid = uid,
                        fullName = namaLengkap,
                        email = email,
                        role = "Member",
                        votesCast = 0,
                        roomsHosted = 0,
                        emailNotifications = true
                    )

                    db.collection("users").document(uid).set(newUserProfile)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onError("Gagal menyimpan data user: ${e.message}") }
                } else {
                    onError(task.exception?.message ?: "Gagal mendaftar")
                }
            }
    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}