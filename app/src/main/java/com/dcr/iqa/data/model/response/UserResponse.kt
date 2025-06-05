package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: String,
    val email: String,
    val username: String,
    val userRole: String
)
