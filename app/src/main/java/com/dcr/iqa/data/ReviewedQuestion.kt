package com.dcr.iqa.data

data class ReviewedQuestion(
    val questionText: String,
    val options: List<Option>,
    val userSelectedOptionId: String?, // Null if not answered
    val correctOptionId: String
)
