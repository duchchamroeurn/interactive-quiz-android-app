package com.dcr.iqa.data.model

data class QuizData(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val questions: List<Question>
)