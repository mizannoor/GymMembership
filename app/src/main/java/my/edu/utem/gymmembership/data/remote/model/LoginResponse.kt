package my.edu.utem.gymmembership.data.remote.model

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String // e.g., "Admin", "Trainer", "Member"
)
