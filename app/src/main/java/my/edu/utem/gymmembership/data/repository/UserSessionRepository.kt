package my.edu.utem.gymmembership.data.repository

import my.edu.utem.gymmembership.data.local.dao.UserSessionDao
import my.edu.utem.gymmembership.data.local.entity.UserSession
import javax.inject.Inject

class UserSessionRepository @Inject constructor(private val userSessionDao: UserSessionDao) {
    suspend fun saveUserSession(token: String, userId: String, role: String) {
        val session = UserSession(userId, token, role)
        userSessionDao.insertUserSession(session)
    }

    suspend fun getUserSession(): UserSession? {
        return userSessionDao.getUserSession()
    }

    suspend fun logout() {
        userSessionDao.clearSession()
    }
}
