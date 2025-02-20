package my.edu.utem.gymmembership.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import my.edu.utem.gymmembership.data.local.AppDatabase
import my.edu.utem.gymmembership.data.local.dao.UserSessionDao
import my.edu.utem.gymmembership.data.remote.ApiService
import my.edu.utem.gymmembership.data.remote.network.AuthInterceptor
import my.edu.utem.gymmembership.data.remote.network.RetrofitClient3000
import my.edu.utem.gymmembership.data.remote.network.RetrofitClient4000
import my.edu.utem.gymmembership.data.repository.UserSessionRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(userSessionRepository: UserSessionRepository): AuthInterceptor {
        return AuthInterceptor(userSessionRepository)
    }

    // ✅ Provide API Service for Login & Register (RetrofitClient3000)
    @Provides
    @Singleton
    @Named("AuthApi") // ✅ Named annotation for Auth API
    fun provideAuthApiService(authInterceptor: AuthInterceptor): ApiService {
        return RetrofitClient3000.create(authInterceptor)
    }

    // ✅ Provide API Service for Membership & Other CRUD (RetrofitClient4000)
    @Provides
    @Singleton
    @Named("CrudApi") // ✅ Named annotation for CRUD API
    fun provideCrudApiService(authInterceptor: AuthInterceptor): ApiService {
        return RetrofitClient4000.create(authInterceptor)
    }

    // ✅ Provide Room Database Instance
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    // ✅ Provide DAO for UserSession Table
    @Provides
    fun provideUserSessionDao(db: AppDatabase): UserSessionDao {
        return db.userSessionDao()
    }
}

