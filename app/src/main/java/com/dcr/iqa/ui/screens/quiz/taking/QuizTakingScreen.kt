package com.dcr.iqa.ui.screens.quiz.taking

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dcr.iqa.ui.components.QuizTopAppBar
import com.dcr.iqa.ui.screens.quiz.overview.QuizFlowViewModel
import com.dcr.iqa.ui.screens.quiz.taking.views.OptionItem
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTakingScreen(
    navController: NavController,
    viewModel: QuizFlowViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val quizDetails = uiState.quizDetails?.quiz ?: return

    val currentQuestion = quizDetails.questions[uiState.currentQuestionIndex]
    val selectedOptionId = uiState.userAnswers[currentQuestion.id]

    // Helper function to format seconds into MM:SS
    fun formatTime(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val remainingSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
    }

    // Listen for the quiz finished state to navigate away
    LaunchedEffect(uiState.quizFinished) {
        if (uiState.quizFinished) {
            navController.navigate("results_screen") {
                popUpTo("quiz_taking") { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) { // Unit key means this runs once on initial composition
        viewModel.startQuizTimerIfNotRunning()
    }

    BackHandler(enabled = true) {
        // We leave this empty to block the back action.
        // The user cannot leave the quiz screen by pressing back.
    }

    Scaffold(
        topBar = {
            QuizTopAppBar(title = quizDetails.title, actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = "Time left")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatTime(uiState.remainingTimeSeconds),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // UI for the progress bar
            val progressValue = if (uiState.totalQuizTimeSeconds > 0) {
                uiState.remainingTimeSeconds.toFloat() / uiState.totalQuizTimeSeconds.toFloat()
            } else {
                0f // Avoid division by zero if totalTime is not yet set or is zero
            }
            val animatedProgress by animateFloatAsState(
                targetValue = progressValue, label = "timerProgress"
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth()
            )

            // Display submission error if it exists
            uiState.submissionError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Question Text & Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = currentQuestion.questionText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .weight(1f)
                    )
                    Text(
                        // Use currentQuestionIndex from uiState and total questions from quizInfo
                        text = "${uiState.currentQuestionIndex + 1}/${quizDetails.questions.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }


                // Options List
                Column(modifier = Modifier.weight(1f)) {
                    currentQuestion.options.forEach { option ->
                        OptionItem(
                            optionText = option.optionText,
                            isSelected = option.id == selectedOptionId,
                            onOptionSelected = {
                                viewModel.selectAnswer(currentQuestion.id, option.id)
                            })
                    }
                }

                // Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Added padding here
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.previousQuestion() },
                        enabled = uiState.currentQuestionIndex > 0 && !uiState.isSubmitting // Disable during submission
                    ) {
                        Text("Previous")
                    }

                    val isLastQuestion = uiState.quizDetails?.quiz?.questions?.let {
                        uiState.currentQuestionIndex == it.size - 1
                    } == true

                    Button(
                        onClick = {
                            if (isLastQuestion) {
                                viewModel.finishQuiz()
                            } else {
                                viewModel.nextQuestion()
                            }
                        },
                        enabled = selectedOptionId != null && !uiState.isSubmitting // Disable during submission
                    ) {
                        if (uiState.isSubmitting && isLastQuestion) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(if (isLastQuestion) "Finish" else "Next")
                        }
                    }
                }
            }
        }
    }
}
