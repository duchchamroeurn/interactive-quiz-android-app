package com.dcr.iqa.ui.screens.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun JoinQuizSection(
    mainNavController: NavController, // Renamed for clarity
    viewModel: JoinQuizViewModel = hiltViewModel()
) {
    var sessionCode by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Observe joinedQuizDetails for navigation
    LaunchedEffect(uiState.joinedQuizDetails) {
        uiState.joinedQuizDetails?.let { details ->
            // Navigate to the quiz overview (or quiz flow) screen with the quiz ID
            mainNavController.navigate("quiz_flow/${details.sessionId}")
            viewModel.onJoinSuccessNavigationConsumed() // Reset state after navigation
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Join a Live Quiz", style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = sessionCode,
                onValueChange = { sessionCode = it },
                label = { Text("Enter Session Code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.joinError != null,
                enabled = !uiState.isLoading
            )
            uiState.joinError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.End))
            } else {
                Button(
                    onClick = { viewModel.onJoinQuizClicked(sessionCode) },
                    modifier = Modifier.align(Alignment.End),
                    enabled = sessionCode.isNotBlank()
                ) {
                    Text("Join")
                }
            }
        }
    }
}