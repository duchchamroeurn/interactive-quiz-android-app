import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dcr.iqa.ui.screens.home.HomeViewModel
import com.dcr.iqa.ui.screens.home.views.PublicQuizItem
import com.dcr.iqa.ui.screens.home.views.QuizEmptyState
import com.dcr.iqa.ui.screens.home.views.QuizItem
import com.dcr.iqa.ui.screens.home.views.SeeAllCard
import com.dcr.iqa.ui.screens.home.views.WelcomeSection

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        // Show a loading indicator in the center
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.error != null) {
        // Show an error message
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
        }
    } else {
        // Your main UI content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WelcomeSection(
                    userName = uiState.username,
                    onJoinQuizClicked = {
                        navController.navigate("join_quiz_screen")
                    })
            }

            if (uiState.publicQuizzes.isNotEmpty()) {
                item {
                    Column {
                        Text(
                            text = "Public Quizzes",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.publicQuizzes) { availableQuiz ->
                                PublicQuizItem(
                                    quiz = availableQuiz.quiz,
                                    onClick = { navController.navigate("quiz_flow/${availableQuiz.sessionId}") }
                                )
                            }
                            // Logic to show "See All" card
                            if (uiState.publicQuizzes.size > 10) { // Or another threshold
                                item {
                                    SeeAllCard(onClick = { /* TODO: Navigate to a dedicated public quizzes list screen */ })
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Quizzes Available For You",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
            }
            if (uiState.availableQuizzes.isNotEmpty()) {
                items(uiState.availableQuizzes) { availableQuiz ->
                    QuizItem(
                        session = availableQuiz,
                        navController = navController,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                item {
                    QuizEmptyState()
                }
            }
        }
    }
}