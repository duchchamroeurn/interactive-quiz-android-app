package com.dcr.iqa.ui.screens.quiz.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dcr.iqa.ui.components.QuizTopAppBar
import com.dcr.iqa.ui.screens.quiz.review.views.QuizReviewContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizReviewScreen(
    navController: NavController,
    viewModel: QuizReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            QuizTopAppBar(navController, title = uiState.reviewData?.quiz?.title ?: "Quiz Review")
        }) { paddingValues ->

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
            }
            uiState.reviewData != null -> {
                QuizReviewContent(modifier = Modifier.padding(paddingValues), uiState.reviewData!!)
            }
        }

    }
}