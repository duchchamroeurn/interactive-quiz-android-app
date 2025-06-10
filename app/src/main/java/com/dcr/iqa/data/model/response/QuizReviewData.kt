package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QuizReviewData(
    val session: SessionInfo,
    val user: UserResponse, // We can reuse the UserData class from login
    val quiz: QuizDetail, // We can reuse the QuizDetail from the overview screen
    val answers: List<AnswerRecord>
)
@Serializable
data class SessionInfo(
    val sessionId: String,
    val sessionCode: String,
    val startTime: String,
    val endTime: String?
)

@Serializable
data class AnswerRecord(
    val questionId: String,
    val answerId: String
)