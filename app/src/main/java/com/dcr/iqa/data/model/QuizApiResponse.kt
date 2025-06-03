package com.dcr.iqa.data.model

data class QuizApiResponse(
    val success: Boolean,
    val data: QuizData,
    val message: String,
    val timestamp: String
)
