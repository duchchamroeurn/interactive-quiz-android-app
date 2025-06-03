package com.dcr.iqa.data.di

import android.content.Context
import com.dcr.iqa.data.respository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes the dependencies live as long as the app does
object AppModule {

    @Provides
    @Singleton // This ensures only one instance of the repository is ever created
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context // Hilt provides the application context automatically
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}