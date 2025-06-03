package com.dcr.iqa.screens.submitted.answers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dcr.iqa.data.SubmittedQuiz
import com.dcr.iqa.screens.main.views.QuizTopAppBar
import com.dcr.iqa.screens.submitted.answers.views.EmptyState
import com.dcr.iqa.screens.submitted.answers.views.SubmittedQuizItem
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmittedAnswersScreen(navController: NavController) {

    // Sample data to display. In a real app, this would come from a ViewModel.
    val submittedQuizzes = listOf(
        SubmittedQuiz("s1", "USA History Quiz", 12, 15, Date()),
        SubmittedQuiz(
            "s2",
            "Science Basics",
            8,
            10,
            Date(System.currentTimeMillis() - 86400000 * 2)
        ), // 2 days ago
        SubmittedQuiz(
            "s3",
            "Movie Trivia",
            18,
            20,
            Date(System.currentTimeMillis() - 86400000 * 5)
        ),
        SubmittedQuiz("s1", "USA History Quiz", 12, 15, Date()),
        SubmittedQuiz(
            "s2",
            "Science Basics",
            8,
            10,
            Date(System.currentTimeMillis() - 86400000 * 2)
        ), // 2 days ago
        SubmittedQuiz(
            "s3",
            "Movie Trivia",
            18,
            20,
            Date(System.currentTimeMillis() - 86400000 * 5)
        ),
        SubmittedQuiz("s1", "USA History Quiz", 12, 15, Date()),
        SubmittedQuiz(
            "s2",
            "Science Basics",
            8,
            10,
            Date(System.currentTimeMillis() - 86400000 * 2)
        ), // 2 days ago
        SubmittedQuiz(
            "s3",
            "Movie Trivia",
            18,
            20,
            Date(System.currentTimeMillis() - 86400000 * 5)
        ) ,        SubmittedQuiz("s1", "USA History Quiz", 12, 15, Date()),
        SubmittedQuiz(
            "s2",
            "Science Basics",
            8,
            10,
            Date(System.currentTimeMillis() - 86400000 * 2)
        ), // 2 days ago
        SubmittedQuiz(
            "s3",
            "Movie Trivia",
            18,
            20,
            Date(System.currentTimeMillis() - 86400000 * 5)
        ),        SubmittedQuiz("s1", "USA History Quiz", 12, 15, Date()),
        SubmittedQuiz(
            "s2",
            "Science Basics",
            8,
            10,
            Date(System.currentTimeMillis() - 86400000 * 2)
        ), // 2 days ago
        SubmittedQuiz(
            "s3",
            "Movie Trivia",
            18,
            20,
            Date(System.currentTimeMillis() - 86400000 * 5)
        )
    )

    // Set this to true to see the empty state UI
    val showEmptyState = false

    Scaffold(
        topBar = {
            QuizTopAppBar(navController, title = "My Submissions")
        }
    ) { paddingValues ->
        if (showEmptyState) {
            EmptyState(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(submittedQuizzes) { quiz ->
                    SubmittedQuizItem(
                        submittedQuiz = quiz,
                        onClick = {
                            navController.navigate("quiz_review")
                        }
                    )
                }
            }
        }
    }
}