package com.dcr.iqa.data.respository

import android.content.SharedPreferences
import com.dcr.iqa.data.model.response.UserResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class UserSessionManager @Inject constructor(
    private val prefs: SharedPreferences,
    private val json: Json
) {
    companion object {
        private const val KEY_USER_DATA = "user_data_json"
    }

    /**
     * Saves the user data object by converting it to a JSON string.
     */
    fun saveUser(user: UserResponse) {
        val userJson = json.encodeToString(user)
        prefs.edit { putString(KEY_USER_DATA, userJson) }
    }

    /**
     * Retrieves the user data object by reading the JSON string and parsing it back.
     * Returns null if no user is saved.
     */
    fun getUser(): UserResponse? {
        val userJson = prefs.getString(KEY_USER_DATA, null)
        return try {
            userJson?.let { json.decodeFromString<UserResponse>(it) }
        } catch (e: Exception) {
            // In case of a parsing error (e.g., app update changes the data class)
            // returning null is a safe fallback.
            null
        }
    }

    /**
     * Clears the saved user data, effectively logging the user out.
     */
    fun clearSession() {
        prefs.edit { remove(KEY_USER_DATA) }
    }
}