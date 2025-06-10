package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QuizSubmissionSummary(
    val sessionId: String,
    val sessionCode: String,
    val total: Double, // Using Double as JSON has a decimal
    val numberCorrectAnswer: Double,
    val quizTitle: String,
    val submittedDate: String // Keep as String for now, format in UI
)
