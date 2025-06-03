package com.dcr.iqa.ui.screens.quiz.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dcr.iqa.data.model.Option
import com.dcr.iqa.data.model.ReviewedQuestion
import com.dcr.iqa.ui.components.QuizTopAppBar
import com.dcr.iqa.ui.screens.quiz.review.views.ReviewedQuestionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizReviewScreen(navController: NavController) {

    // --- Sample Data: In a real app, this would be fetched based on a submissionId ---
    val reviewData = listOf(
        ReviewedQuestion(
            questionText = "Who was the primary author of the Declaration of Independence?",
            userSelectedOptionId = "opt3", // User chose Thomas Jefferson
            correctOptionId = "opt3",
            options = listOf(
                Option("opt1", "Benjamin Franklin", false),
                Option("opt2", "George Washington", false),
                Option("opt3", "Thomas Jefferson", false),
                Option("opt4", "John Adams", false)
            )
        ), ReviewedQuestion(
            questionText = "The American Civil War began in 1861.",
            userSelectedOptionId = "opt2", // User chose False
            correctOptionId = "opt1",
            options = listOf(
                Option("opt1", "True", true), Option("opt2", "False", false)
            )
        )
    )
    val score = reviewData.count { it.userSelectedOptionId == it.correctOptionId }
    val totalQuestions = reviewData.size
    // --- End of Sample Data ---

    Scaffold(
        topBar = {
            QuizTopAppBar(navController, title = "Review: USA History Quiz")
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with the final score
            item {
                Text(
                    "Your Score: $score / $totalQuestions",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // List of questions and their reviewed answers
            itemsIndexed(reviewData) { index, question ->
                ReviewedQuestionItem(question = question, questionNumber = index + 1)
            }
        }
    }
}