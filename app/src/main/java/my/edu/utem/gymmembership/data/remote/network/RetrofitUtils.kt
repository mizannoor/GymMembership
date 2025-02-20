package my.edu.utem.gymmembership.data.remote.network

import my.edu.utem.gymmembership.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofitInstance(baseUrl: String, authInterceptor: AuthInterceptor): ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor) // ✅ Attach AuthInterceptor for JWT token
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient) // ✅ Use OkHttpClient with AuthInterceptor
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
