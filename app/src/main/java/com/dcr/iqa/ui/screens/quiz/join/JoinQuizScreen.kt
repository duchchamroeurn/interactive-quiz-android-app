package com.dcr.iqa.ui.screens.quiz.join

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dcr.iqa.ui.components.QuizTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinQuizScreen(
    navController: NavController,
    viewModel: JoinQuizViewModel = hiltViewModel() // Use the existing ViewModel
) {
    var sessionCodeInput by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // This effect listens for a successful join to navigate to the quiz overview
    LaunchedEffect (uiState.joinedQuizDetails) {
        uiState.joinedQuizDetails?.let { details ->
            navController.navigate("quiz_flow/${details.sessionId}")
            viewModel.onJoinSuccessNavigationConsumed() // Reset state to prevent re-navigation
        }
    }

    Scaffold (
        topBar = {
            QuizTopAppBar(navController, title = "Join a Live Quiz")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Enter the session code provided by your host to join the quiz.",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = sessionCodeInput,
                onValueChange = { sessionCodeInput = it },
                label = { Text("Session Code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.joinError != null,
                enabled = !uiState.isLoading
            )

            uiState.joinError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button (
                    onClick = { viewModel.onJoinQuizClicked(sessionCodeInput) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = sessionCodeInput.isNotBlank()
                ) {
                    Text("Join Quiz")
                }
            }
        }
    }
}