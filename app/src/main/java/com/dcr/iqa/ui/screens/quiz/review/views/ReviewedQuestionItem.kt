package com.dcr.iqa.ui.screens.quiz.review.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcr.iqa.data.model.ReviewedQuestion

@Composable
fun ReviewedQuestionItem(question: ReviewedQuestion, questionNumber: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Question $questionNumber: ${question.questionText}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            question.options.forEach { option ->
                ReviewedOptionRow(
                    optionText = option.optionText,
                    isCorrect = option.id == question.correctOptionId,
                    isSelected = option.id == question.userSelectedOptionId
                )
            }
        }
    }
}