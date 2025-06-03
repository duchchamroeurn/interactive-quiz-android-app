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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dcr.iqa.data.model.QuizData
import com.dcr.iqa.ui.components.QuizTopAppBar
import com.dcr.iqa.ui.screens.quiz.taking.views.OptionItem
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTakingScreen(
    navController: NavController, quizData: QuizData, remainingTime: Int, // Receives remaining time
    totalTime: Int      // Receives total time for the progress bar
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, String>() }
    val currentQuestion = quizData.questions[currentQuestionIndex]
    val selectedOptionId = userAnswers[currentQuestion.id]

    // Helper function to format seconds into MM:SS
    fun formatTime(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val remainingSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.US, "%02d:%02d", minutes, remainingSeconds)
    }

    BackHandler (enabled = true) {
        // We leave this empty to block the back action.
        // The user cannot leave the quiz screen by pressing back.
    }

    Scaffold(
        topBar = {
            QuizTopAppBar(title = quizData.title, actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = "Time left")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatTime(remainingTime),
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
            // 3. UI for the progress bar
            val progress by animateFloatAsState(
                targetValue = remainingTime.toFloat() / totalTime.toFloat(), label = "timerProgress"
            )
            LinearProgressIndicator(
                progress = { progress }, modifier = Modifier.fillMaxWidth()
            )

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
                        text = "${currentQuestionIndex + 1}/${quizData.questions.size}",
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
                                userAnswers[currentQuestion.id] = option.id
                            })
                    }
                }

                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    OutlinedButton(
                        onClick = { if (currentQuestionIndex > 0) currentQuestionIndex-- },
                        enabled = currentQuestionIndex > 0 // Disabled on first question
                    ) {
                        Text("Previous")
                    }

                    // Next/Finish Button
                    val isLastQuestion = currentQuestionIndex == quizData.questions.lastIndex
                    Button(
                        onClick = {
                            if (isLastQuestion) {
                                navController.navigate("results_screen") {
                                    popUpTo("quiz_taking/${quizData.id}") { inclusive = true }
                                }
                            } else {
                                currentQuestionIndex++
                            }
                        },
                        // Enabled only after an answer is selected
                        enabled = userAnswers.containsKey(currentQuestion.id)
                    ) {
                        Text(if (isLastQuestion) "Finish" else "Next")
                    }
                }
            }
        }
    }
}
