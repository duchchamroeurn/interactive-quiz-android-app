package com.dcr.iqa.screens.submitted.answers.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcr.iqa.data.SubmittedQuiz
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SubmittedQuizItem(
    submittedQuiz: SubmittedQuiz,
    onClick: () -> Unit
) {
    val scorePercentage = submittedQuiz.score.toFloat() / submittedQuiz.totalQuestions.toFloat()
    val formattedDate = remember {
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(submittedQuiz.submissionDate)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Score Ring
            ScoreRing(
                scorePercentage = scorePercentage,
                scoreText = "${submittedQuiz.score}/${submittedQuiz.totalQuestions}"
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Right side: Quiz Details
            Column {
                Text(
                    text = submittedQuiz.quizTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = "Submission Date",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Submitted on $formattedDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}