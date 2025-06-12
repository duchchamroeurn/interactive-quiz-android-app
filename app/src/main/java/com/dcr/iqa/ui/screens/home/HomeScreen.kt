import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dcr.iqa.ui.screens.home.HomeViewModel
import com.dcr.iqa.ui.screens.home.views.QuizItem
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
            item {
                Text(
                    text = "Or Practice a Quiz",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(uiState.availableQuizzes) { availableQuiz ->
                // You might need to adjust QuizItem to accept an AvailableQuiz object
                // or just its nested quiz object.
                QuizItem(
                    session = availableQuiz,
                    navController = navController
                )
            }
        }
    }
}