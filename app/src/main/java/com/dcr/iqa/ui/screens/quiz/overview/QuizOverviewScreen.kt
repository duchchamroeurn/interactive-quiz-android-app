package com.dcr.iqa.ui.screens.quiz.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dcr.iqa.data.model.response.QuizSessionDetails
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun QuizOverviewScreen(
    navController: NavController,
    viewModel: QuizFlowViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            // Loading State
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            // Error State
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
        }

        uiState.quizDetails != null -> {
            // Success State - pass the details to the content
            val details = uiState.quizDetails!!
            QuizOverviewContent(navController = navController, details = details)
        }
    }

}

@Composable
fun QuizOverviewContent(navController: NavController, details: QuizSessionDetails) {
    val quiz = details.quiz
    val questionCount = quiz.questions.size
    // Calculate total duration by summing up time for each question
    val totalDurationSeconds = quiz.questions.sumOf { it.time }
    val durationMinutes = (totalDurationSeconds / 60)
    Scaffold (content =  { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(paddingValue),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Section 1: Title and Description
            item {
                QuizInfoSection(
                    title = quiz.title,
                    description = quiz.description
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Section 2: Stats (Questions and Duration)
            item {
                QuizStatsSection(
                    questionCount = questionCount,
                    durationMinutes = durationMinutes
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Section 3: Important Instructions
            item {
                InstructionsSection(
                    questionCount = questionCount,
                    durationMinutes = durationMinutes
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Section 4: Action Buttons
            item {
                ActionButtons(
                    onTryLater = { navController.popBackStack() },
                    onGetStarted = { navController.navigate("quiz_taking") }
                )
            }
        }
    })
}

@Composable
private fun QuizInfoSection(title: String, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuizStatsSection(questionCount: Int, durationMinutes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            InfoChip(icon = Icons.Default.Info, label = "Questions", value = "$questionCount")
            InfoChip(icon = Icons.Default.Timer, label = "Duration", value = "$durationMinutes min")
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}


@Composable
private fun InstructionsSection(questionCount: Int, durationMinutes: Int) {
    // Helper to format the time, same as before
    val timeFormatted = remember(durationMinutes) {
        val minutes = TimeUnit.MINUTES.toMinutes(durationMinutes.toLong())
        val seconds = TimeUnit.MINUTES.toSeconds(durationMinutes.toLong()) % 60
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }

    // A list of all our instructions, using AnnotatedString for styling
    val instructions = remember(questionCount, timeFormatted) {
        listOf(
            buildAnnotatedString {
                append("This quiz contains ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$questionCount questions")
                }
                append(".")
            },
            buildAnnotatedString {
                append("You have ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(timeFormatted)
                }
                append(" minutes to complete the quiz.")
            },
            buildAnnotatedString {
                append("The quiz will ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("automatically submit")
                }
                append(" once the time limit expires.")
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("DO NOT")
                }
                append(" close the app after starting the quiz.")
            },
            buildAnnotatedString {
                append("This quiz can be attempted ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("only once")
                }
                append(" per user.")
            },
            buildAnnotatedString {
                append("Make sure you have a stable internet connection.")
            }
        )
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            // Use a theme color that can be configured to a "warning" or "information" shade.
            // tertiaryContainer is often a good candidate for this.
            // The actual color depends on your app's Theme.kt
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer // Ensures good text contrast
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Add padding inside the card
        ) {
            Text(
                text = "Important Instructions:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
                // Text color will be taken from CardDefaults.contentColor
            )
            Spacer(modifier = Modifier.height(12.dp)) // Increased space
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                instructions.forEach { instruction ->
                    InstructionRow(annotatedString = instruction)
                }
            }
        }
    }
}

/**
 * A helper composable for displaying a single row in the instruction list.
 */
@Composable
private fun InstructionRow(annotatedString: AnnotatedString) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        // The bullet point
        Text(
            text = "\u2022", // This is the unicode for a bullet point •
            modifier = Modifier.padding(end = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // The instruction text
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun ActionButtons(onTryLater: () -> Unit, onGetStarted: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedButton(onClick = onTryLater) {
            Text("Try Later")
        }
        Button(onClick = onGetStarted) {
            Text("Get Started")
        }
    }
}