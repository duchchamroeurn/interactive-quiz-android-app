package com.dcr.iqa.data.respository

import com.dcr.iqa.data.model.request.LoginRequest
import com.dcr.iqa.data.remote.AuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: AuthService,
    private val sessionManager: UserSessionManager
) {
    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data
                if (user != null) {
                    sessionManager.saveUser(user)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("User ID not found in response"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "An unknown error occurred"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        }
    }
}