package my.edu.utem.gymmembership.data.repository

import my.edu.utem.gymmembership.data.remote.ApiService
import my.edu.utem.gymmembership.data.remote.model.LoginRequest
import my.edu.utem.gymmembership.data.remote.model.LoginResponse
import my.edu.utem.gymmembership.data.remote.model.RegisterRequest
import my.edu.utem.gymmembership.data.remote.model.RegisterResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class AuthRepository @Inject constructor(@Named("AuthApi") private val apiService: ApiService) {
    suspend fun login(credentials: LoginRequest): Response<LoginResponse> = apiService.login(credentials)
    suspend fun register(registration: RegisterRequest): Response<RegisterResponse> = apiService.register(registration)
}