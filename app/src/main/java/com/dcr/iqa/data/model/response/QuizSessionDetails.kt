package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QuizSessionDetails(
    val sessionId: String,
    val sessionCode: String,
    val startTime: String,
    val endTime: String?,
    val quiz: QuizDetail
)
