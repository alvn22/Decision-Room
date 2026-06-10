package id.ac.pnm.decisionroom.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import id.ac.pnm.decisionroom.model.auth.UserProfile

class AuthViewModel : ViewModel() {
    // Inisialisasi Firebase Authentication untuk mengurus proses login/register
    private val auth = Firebase.auth
    // Inisialisasi Firebase Firestore untuk menyimpan/membaca data profil user
    private val db = Firebase.firestore

    // Fungsi untuk mengecek apakah saat ini ada user yang sedang login di perangkat
    fun isUserLoggedIn(): Boolean {
        // Jika currentUser tidak null, berarti ada user yang login (return true)
        return auth.currentUser != null
    }

    // Fungsi untuk proses Login
    // Menerima parameter email, password, dan dua callback (onSuccess & onError) untuk memberi tahu UI hasilnya
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // 1. Validasi Input: Pastikan email dan password tidak kosong
        if (email.isEmpty() || password.isEmpty()) {
            onError("Email dan Password wajib diisi")
            return // Hentikan eksekusi fungsi jika input kosong
        }

        // 2. Eksekusi Login ke Firebase Auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Mengecek apakah proses dari Firebase selesai
                if (task.isSuccessful) {
                    // Jika login berhasil, panggil callback onSuccess() ke UI
                    onSuccess()
                } else {
                    // Jika gagal, tangkap pesan errornya dan kirim ke UI lewat onError()
                    onError(task.exception?.message ?: "Terjadi kesalahan yang tidak diketahui")
                }
            }
    }

    // Fungsi untuk proses Registrasi (Daftar Akun Baru)
    fun register(
        namaLengkap: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // 1. Validasi Input: Pastikan semua kolom sudah diisi oleh user
        if (email.isEmpty() || password.isEmpty() || namaLengkap.isEmpty()) {
            onError("Semua data wajib diisi")
            return
        }

        // 2. Eksekusi Registrasi ke Firebase Auth (Membuat akun untuk bisa login)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // JIKA REGISTRASI AUTH BERHASIL:

                    // Ambil UID (Unique ID) yang baru saja di-generate oleh Firebase Auth
                    // Jika karena suatu hal UID null, beri nilai default string kosong ("")
                    val uid = auth.currentUser?.uid ?: ""

                    // Buat cetak biru (blueprint) data user menggunakan model UserProfile
                    // UID Auth tadi dijadikan parameter untuk menyimpan relasinya
                    val newUserProfile = UserProfile(
                        uid = uid,
                        fullName = namaLengkap,
                        email = email,
                        role = "Member",       // Role default untuk user baru
                        votesCast = 0,         // Statistik awal diatur ke 0
                        roomsHosted = 0,       // Statistik awal diatur ke 0
                        emailNotifications = true
                    )

                    // 3. Simpan data profil ke database Firestore
                    // Masuk ke koleksi "users", lalu buat dokumen dengan ID yang sama persis dengan UID Auth
                    db.collection("users").document(uid).set(newUserProfile)
                        .addOnSuccessListener {
                            // Jika berhasil menyimpan ke database, beri tahu UI bahwa semua proses sukses
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            // Jika sukses buat akun Auth TAPI gagal simpan ke database, kirim error
                            onError("Gagal menyimpan data user: ${e.message}")
                        }
                } else {
                    // Jika dari awal pembuatan akun Auth sudah gagal (misal email sudah terdaftar/password kurang kuat)
                    onError(task.exception?.message ?: "Gagal mendaftar")
                }
            }
    }

    // Fungsi untuk mengeluarkan user dari sesi saat ini
    fun logout(onSuccess: () -> Unit) {
        // Hapus sesi di Firebase Auth
        auth.signOut()
        // Beri tahu UI bahwa proses logout sudah selesai (misal untuk pindah halaman ke LoginScreen)
        onSuccess()
    }
}
