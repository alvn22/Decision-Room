package id.ac.pnm.decisionroom.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.model.auth.UserProfile // Pastikan model ini di-import

class ProfileViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    // FUNGSI BARU: Untuk Mengambil Data Profil (Termasuk Statistik Room)

    fun getUserProfile(
        onSuccess: (UserProfile) -> Unit, // Mengembalikan objek UserProfile ke UI jika sukses
        onError: (String) -> Unit         // Mengembalikan pesan error jika gagal
    ) {
        // Ambil UID dari user yang sedang login saat ini
        val uid = auth.currentUser?.uid

        // Validasi: Pastikan ada user yang login
        if (uid == null) {
            onError("Sesi salah, silakan login kembali")
            return
        }

        // Membaca data dari Firestore: Masuk ke koleksi "users", cari dokumen sesuai UID
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                // Jika dokumen berhasil ditarik dari database
                if (document != null && document.exists()) {
                    // Konversi dokumen Firestore menjadi objek UserProfile Kotlin (Mapping)
                    // Di sinilah roomsHosted dan votesCast otomatis ikut terambil!
                    val userProfile = document.toObject(UserProfile::class.java)

                    if (userProfile != null) {
                        onSuccess(userProfile) // Kirim data lengkap ke UI
                    } else {
                        onError("Gagal memproses struktur data profil")
                    }
                } else {
                    onError("Data profil tidak ditemukan di database")
                }
            }
            .addOnFailureListener { e ->
                // Jika koneksi internet putus atau gagal mengambil data
                onError("Gagal mengambil data profil: ${e.message}")
            }
    }

    // FUNGSI  Untuk Memperbarui Data
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