package id.ac.pnm.decisionroom.model.auth

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "Member", // Nilai default untuk pengguna baru
    val votesCast: Int = 0,
    val roomsHosted: Int = 0,
    val emailNotifications: Boolean = true,
    val profileImageUrl: String = "" // Disiapkan jika nanti ada fitur unggah foto
)