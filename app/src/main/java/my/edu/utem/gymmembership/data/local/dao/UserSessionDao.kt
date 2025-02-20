package my.edu.utem.gymmembership.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import my.edu.utem.gymmembership.data.local.entity.UserSession

@Dao
interface UserSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSession(userSession: UserSession)

    @Query("SELECT * FROM user_session LIMIT 1")
    suspend fun getUserSession(): UserSession?

    @Query("DELETE FROM user_session")
    suspend fun clearSession()
}
