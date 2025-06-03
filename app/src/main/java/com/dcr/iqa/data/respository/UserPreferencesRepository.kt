package com.dcr.iqa.data.respository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository (private val context: Context) {
    // Define a key for our boolean flag
    private val WELCOME_SCREEN_SHOWN_KEY = booleanPreferencesKey("welcome_screen_shown")

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

    /**
     * A Flow that emits true if the welcome screen has been shown, and false otherwise.
     * It defaults to 'false' if the key doesn't exist, which is the "first time" case.
     */
    val hasSeenWelcomeScreen: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[WELCOME_SCREEN_SHOWN_KEY] == true
        }

    // It emits the token string if it exists, or null if the user is not logged in.
    val authToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }

    /**
     * A suspend function to update the flag in DataStore.
     * This will be called after the user completes the welcome screen.
     */
    suspend fun setWelcomeScreenShown(hasBeenShown: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WELCOME_SCREEN_SHOWN_KEY] = hasBeenShown
        }
    }

    // --- NEW: Function to save the auth token ---
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    // --- NEW: Function to clear the auth token on logout ---
    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }
}