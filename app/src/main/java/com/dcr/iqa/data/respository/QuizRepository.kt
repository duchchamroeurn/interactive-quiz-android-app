package com.dcr.iqa.data.respository

import com.dcr.iqa.data.model.response.AvailableQuiz
import com.dcr.iqa.data.model.response.QuizSessionDetails
import com.dcr.iqa.data.remote.QuizService
import com.dcr.iqa.data.remote.SessionService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizService: QuizService,
    private val  sessionService: SessionService
) {
    /**
     * A new function to get quizzes for a specific user by their ID.
     */
    suspend fun getAvailableQuizzesForUser(userId: String): Result<List<AvailableQuiz>> {
        return try {
            val response = quizService.getAvailableQuizzesForUser(userId) // Pass the userId to the service
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("API returned success=false for user quizzes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuizDetails(sessionId: String): Result<QuizSessionDetails> {
        return try {
            val response = sessionService.getQuizDetailsBySession(sessionId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinQuizBySessionCode(sessionCode: String): Result<QuizSessionDetails> {
        return try {
            val response = sessionService.joinQuizBySessionCode(sessionCode)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}