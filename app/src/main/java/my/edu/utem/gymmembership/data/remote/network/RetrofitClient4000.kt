package my.edu.utem.gymmembership.data.remote.network

import my.edu.utem.gymmembership.BuildConfig
import my.edu.utem.gymmembership.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient4000 {

    fun create(authInterceptor: AuthInterceptor): ApiService {
        val baseUrl = BuildConfig.CRUD_BASE_URL // ✅ Get BASE_URL from BuildConfig
        return createRetrofitInstance(baseUrl, authInterceptor);
    }
}