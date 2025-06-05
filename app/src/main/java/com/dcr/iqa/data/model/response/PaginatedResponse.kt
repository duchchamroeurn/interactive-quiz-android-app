package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val success: Boolean,
    val data: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val timestamp: String
)
