package my.edu.utem.gymmembership.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.edu.utem.gymmembership.data.remote.model.LoginRequest
import my.edu.utem.gymmembership.data.remote.model.LoginResponse
import my.edu.utem.gymmembership.data.remote.model.RegisterRequest
import my.edu.utem.gymmembership.data.remote.model.RegisterResponse
import my.edu.utem.gymmembership.data.repository.AuthRepository
import my.edu.utem.gymmembership.data.repository.UserSessionRepository
import retrofit2.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var loginResult: ((Response<LoginResponse>) -> Unit)? = null
    var registerResult: ((Response<RegisterResponse>) -> Unit)? = null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.login(LoginRequest(email, password))
            loginResult?.invoke(response)

            if (response.isSuccessful) {
                response.body()?.let {
                    // ✅ Save token to Room Database
                    userSessionRepository.saveUserSession(it.token, it.userId, it.role)
                }
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.register(RegisterRequest(username, email, password))
            registerResult?.invoke(response)
        }
    }
}
