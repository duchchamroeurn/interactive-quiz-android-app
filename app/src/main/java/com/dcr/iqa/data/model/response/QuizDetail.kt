package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QuizDetail(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val questions: List<QuestionDetail>
)
