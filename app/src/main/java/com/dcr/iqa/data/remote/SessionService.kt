package com.dcr.iqa.data.remote

import com.dcr.iqa.data.model.response.QuizSessionDetails
import com.dcr.iqa.data.model.response.SuccessResponse
import retrofit2.http.GET
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
}