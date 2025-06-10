package com.dcr.iqa.ui.screens.quiz.review.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcr.iqa.data.model.response.QuizReviewData


@Composable
fun QuizReviewContent(modifier: Modifier = Modifier, reviewData: QuizReviewData) {
    // For efficient lookup, convert the list of answers into a Map
    val userAnswersMap = remember (reviewData.answers) {
        reviewData.answers.associateBy({ it.questionId }, { it.answerId })
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val score = reviewData.answers.count { answerRecord ->
            val question = reviewData.quiz.questions.find { it.id == answerRecord.questionId }
            val correctOption = question?.options?.find { it.correct }
            correctOption?.id == answerRecord.answerId
        }
        val totalQuestions = reviewData.quiz.questions.size

        // Header with the final score
        item {
            Text(
                "Your Score: $score / $totalQuestions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // List of questions and their reviewed answers
        itemsIndexed(reviewData.quiz.questions) { index, question ->
            val userSelectedOptionId = userAnswersMap[question.id]
            ReviewedQuestionItem(
                question = question,
                questionNumber = index + 1,
                userSelectedOptionId = userSelectedOptionId
            )
        }
    }
}