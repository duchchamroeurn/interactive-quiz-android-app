import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dcr.iqa.data.Quiz
import com.dcr.iqa.screens.home.views.JoinQuizSection
import com.dcr.iqa.screens.home.views.QuizItem
import com.dcr.iqa.screens.home.views.WelcomeSection

@Composable
fun HomeScreen(navController: NavController, quizzes: List<Quiz>) {
    // In a real app, you would get this from a ViewModel
    val userName = "Alex"
    // LazyColumn makes the whole screen scrollable and is efficient for lists.
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Adds space between items
    ) {
        item {
            WelcomeSection(userName = userName)
        }

        item {
            JoinQuizSection()
        }

        item {
            Text(
                text = "Or Practice a Quiz",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(quizzes) { quiz ->
            QuizItem(
                quiz = quiz,
                navController = navController // Pass the NavController
            )
        }
    }
}