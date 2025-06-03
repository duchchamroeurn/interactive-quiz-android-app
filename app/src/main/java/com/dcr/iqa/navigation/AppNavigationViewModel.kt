package com.dcr.iqa.navigation

import androidx.lifecycle.ViewModel
import com.dcr.iqa.data.respository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    fun loadStartDestination(): String {
        val hasSeenWelcome = runBlocking { userPreferencesRepository.hasSeenWelcomeScreen.first() }
        val authToken = runBlocking { userPreferencesRepository.authToken.first() }

        return when {
            !hasSeenWelcome -> "welcome"       // First priority: show welcome screen if never seen
            authToken != null -> "main_screen"  // If welcome seen AND user is logged in, go to main
            else -> "login"                     // If welcome seen but NOT logged in, go to login
        }
    }
}