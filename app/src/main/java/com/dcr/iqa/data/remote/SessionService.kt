package com.dcr.iqa.data.remote

import com.dcr.iqa.data.model.request.UserSubmitAnswersRequest
import com.dcr.iqa.data.model.response.PaginatedResponse
import com.dcr.iqa.data.model.response.QuizReviewData
import com.dcr.iqa.data.model.response.QuizSessionDetails
import com.dcr.iqa.data.model.response.QuizSubmissionSummary
import com.dcr.iqa.data.model.response.SuccessResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SessionService {

    // Assumes an endpoint that takes a quiz ID and returns its details
    @GET("api/v1/session/{sessionId}")
    suspend fun getQuizDetailsBySession(
        @Path("sessionId") sessionId: String
    ): SuccessResponse<QuizSessionDetails>

    @GET("api/v1/session/code/{code}") // Uses the session code as a path parameter
    suspend fun joinQuizBySessionCode(
        @Path("code") sessionCode: String
    ): SuccessResponse<QuizSessionDetails>

    @POST("api/v1/answer/session/{sessionId}/user/{userId}")
    suspend fun submitQuizAnswers(
        @Path("sessionId") sessionId: String,
        @Path("userId") userId: String,
        @Body requestBody: UserSubmitAnswersRequest
    ): SuccessResponse<String>

    @GET("api/v1/answer/user/{userId}") // Example endpoint
    suspend fun getMySubmissions(
        @Path("userId") userId: String
    ): PaginatedResponse<QuizSubmissionSummary>

    @GET("api/v1/answer/session/{sessionId}/user/{userId}") // Example endpoint for getting a review
    suspend fun getQuizReviewDetails(
        @Path("sessionId") sessionId: String,
        @Path("userId") userId: String,
    ): SuccessResponse<QuizReviewData>
}