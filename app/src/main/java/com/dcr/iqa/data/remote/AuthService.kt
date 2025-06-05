package com.dcr.iqa.data.remote

import com.dcr.iqa.data.model.request.LoginRequest
import com.dcr.iqa.data.model.response.AvailableQuiz
import com.dcr.iqa.data.model.response.PaginatedResponse
import com.dcr.iqa.data.model.response.SuccessResponse
import com.dcr.iqa.data.model.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST("/api/v1/auth/sign-in")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<SuccessResponse<UserResponse>>
}