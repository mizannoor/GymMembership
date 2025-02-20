package my.edu.utem.gymmembership.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import my.edu.utem.gymmembership.data.local.dao.UserSessionDao
import my.edu.utem.gymmembership.data.local.entity.UserSession

@Database(entities = [UserSession::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userSessionDao(): UserSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_membership_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
