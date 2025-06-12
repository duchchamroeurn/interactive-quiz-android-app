package com.dcr.iqa.data.remote

import com.dcr.iqa.data.model.response.AvailableQuiz
import com.dcr.iqa.data.model.response.PaginatedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizService {

    /**
     * Fetches the available quizzes for a specific user.
     * The {userId} in the URL will be replaced by the value of the userId parameter.
     */
    @GET("/api/v1/user/{userId}/quizzes/available")
    suspend fun getAvailableQuizzesForUser(
        @Path("userId") userId: String,
        @Query("type") type: String,
    ): PaginatedResponse<AvailableQuiz>
}