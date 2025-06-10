package com.dcr.iqa.data.di

import android.content.Context
import android.content.SharedPreferences
import com.dcr.iqa.data.remote.AuthService
import com.dcr.iqa.data.remote.QuizService
import com.dcr.iqa.data.remote.SessionService
import com.dcr.iqa.data.respository.UserPreferencesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes the dependencies live as long as the app does
object AppModule {

    private const val BASE_URL = "http://10.10.100.56:9099/"

    @Provides
    @Singleton // This ensures only one instance of the repository is ever created
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context // Hilt provides the application context automatically
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true // Be lenient with new fields from the server
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // This logger is great for debugging. It prints network requests and responses in Logcat.
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("quiz_app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideQuizService(retrofit: Retrofit): QuizService {
        return retrofit.create(QuizService::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionService(retrofit: Retrofit): SessionService {
        return retrofit.create(SessionService::class.java)
    }
}