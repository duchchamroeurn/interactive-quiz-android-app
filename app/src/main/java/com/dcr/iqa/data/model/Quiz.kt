package com.dcr.iqa.data.model

data class Quiz(
    val id: String,
    val title: String,
    val description: String,
    val questionCount: Int,
    val durationMinutes: Int // Add this line
)
