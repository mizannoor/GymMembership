package my.edu.utem.gymmembership.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_session")
data class UserSession(
    @PrimaryKey val userId: String, // Use userId as Primary Key
    val token: String,
    val role: String
)
