package my.edu.utem.gymmembership.data.remote.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    // Optionally include additional fields like firstName, lastName, etc.
)
