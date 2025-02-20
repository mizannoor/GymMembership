package my.edu.utem.gymmembership.data.remote.network

import kotlinx.coroutines.runBlocking
import my.edu.utem.gymmembership.data.repository.UserSessionRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // ✅ Skip Authorization for login & register
        val path = request.url.encodedPath
        if (path.contains("/login") || path.contains("/register")) {
            return chain.proceed(request)
        }

        // ✅ Retrieve JWT Token from Room Database
        val token = runBlocking { userSessionRepository.getUserSession()?.token }

        val newRequest = request.newBuilder()
            .apply {
                token?.let {
                    addHeader("Authorization", "Bearer $it") // ✅ Automatically add token
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}
