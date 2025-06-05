package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String,
    val timestamp: String
)
