package com.dcr.iqa.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class JoinSessionRequest(
    val sessionCode: String,
    val userId: String
)
