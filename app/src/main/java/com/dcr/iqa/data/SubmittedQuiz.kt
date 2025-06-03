package com.dcr.iqa.data

import java.util.Date

data class SubmittedQuiz(
    val submissionId: String,
    val quizTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val submissionDate: Date
)
