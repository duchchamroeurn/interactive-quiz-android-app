package com.dcr.iqa.data.respository

import com.dcr.iqa.data.model.request.SubmittedAnswerRequest
import com.dcr.iqa.data.model.request.UserSubmitAnswersRequest
import com.dcr.iqa.data.model.response.AvailableQuiz
import com.dcr.iqa.data.model.response.QuizReviewData
import com.dcr.iqa.data.model.response.QuizSessionDetails
import com.dcr.iqa.data.model.response.QuizSubmissionSummary
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

    suspend fun submitQuizAnswers(
        sessionId: String,
        userId: String,
        answers: List<SubmittedAnswerRequest>
    ): Result<String> { // The String here is the success message or data from SuccessResponse<String>
        return try {
            val request = UserSubmitAnswersRequest(answers = answers)
            val response = sessionService.submitQuizAnswers(sessionId, userId, request)
            if (response.success) {
                Result.success(response.data ?: response.message) // Prioritize data, fallback to message
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMySubmissions(userId: String): Result<List<QuizSubmissionSummary>> {
        return try {
            val response = sessionService.getMySubmissions(userId)
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("API returned success=false"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuizReviewDetails(sessionId: String, userId: String): Result<QuizReviewData> {
        return try {
            val response = sessionService.getQuizReviewDetails(sessionId, userId)
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