package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AvailableQuiz(
    val sessionId: String,
    val quiz: Quiz
)
