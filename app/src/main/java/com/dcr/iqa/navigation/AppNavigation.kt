package com.dcr.iqa.navigation

import LoginScreen
import MainScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.dcr.iqa.WelcomeScreen
import com.dcr.iqa.data.model.Option
import com.dcr.iqa.data.model.Question
import com.dcr.iqa.data.model.Quiz
import com.dcr.iqa.data.model.QuizData
import com.dcr.iqa.ui.screens.quiz.overview.QuizFlowViewModel
import com.dcr.iqa.ui.screens.quiz.overview.QuizOverviewScreen
import com.dcr.iqa.ui.screens.quiz.result.QuizResultsScreen
import com.dcr.iqa.ui.screens.quiz.review.QuizReviewScreen
import com.dcr.iqa.ui.screens.quiz.taking.QuizTakingScreen
import com.dcr.iqa.ui.screens.submitted.answers.SubmittedAnswersScreen
import kotlinx.coroutines.delay

@Composable
fun QuizApp(viewModel: AppNavigationViewModel = hiltViewModel()) {

    val startDestination = viewModel.loadStartDestination()

    // This is the primary NavController for the whole app
    val primaryNavController = rememberNavController()

    val availableQuizzes = remember {
        listOf(
            Quiz("q1", "History 101", "A basic quiz about world history.", 10, 5),
            Quiz("q2", "Science Basics", "Test your knowledge of fundamental science.", 15, 10),
            Quiz("q3", "Movie Trivia", "How well do you know your movies?", 20, 10)
        )
    }
    NavHost(navController = primaryNavController, startDestination = startDestination) {
        composable("welcome") {
            WelcomeScreen(navController = primaryNavController)
        }
        composable("login") {
            LoginScreen(navController = primaryNavController)
        }
        // Add the new route to our screen that contains the tabs
        composable("main_screen") {
            MainScreen(mainNavController = primaryNavController, quizzes = availableQuizzes)
        }
        quizFlowGraph(primaryNavController)
//        composable("quiz_overview/{sessionId}") {
//            QuizOverviewScreen(navController = primaryNavController)
//        }
//        composable("quiz_taking/{quizId}") { backStackEntry ->
//            val quizId = backStackEntry.arguments?.getString("quizId")
//
//            // --- In a real app, you'd make a network call with quizId. ---
//            // For this example, we'll just use the hardcoded quiz data you provided.
//            // We'll pretend we "fetched" it.
//            val quizData = getHardcodedQuizData() // A helper function to get the data
//
//            // --- NEW GLOBAL TIMER LOGIC ---
//            // 1. Calculate the total time for the entire quiz in seconds.
//            // We assume the total time is the sum of times for all questions.
//            val totalQuizTimeInSeconds = remember { quizData.questions.sumOf { it.time } }
//
//            // 2. State for the global timer.
//            var remainingTimeInSeconds by remember { mutableIntStateOf(totalQuizTimeInSeconds) }
//
//            // 3. This effect runs ONCE when the quiz starts. `key1 = Unit` ensures it doesn't restart.
//            LaunchedEffect(key1 = Unit) {
//                while (remainingTimeInSeconds > 0) {
//                    delay(1000L)
//                    remainingTimeInSeconds--
//                }
//                // When the timer hits 0, auto-submit by navigating to the results screen.
//                primaryNavController.navigate("results_screen") {
//                    // Clear the quiz screen from the back stack
//                    popUpTo("quiz_taking/$quizId") { inclusive = true }
//                }
//            }
//
//            if (quizId != null) {
//                QuizTakingScreen(
//                    navController = primaryNavController,
//                    quizData = quizData,
//                    remainingTime = remainingTimeInSeconds,
//                    totalTime = totalQuizTimeInSeconds
//                )
//            } else {
//                primaryNavController.popBackStack()
//            }
//        }

        composable("results_screen") {
            QuizResultsScreen(navController = primaryNavController)
        }
        composable("submitted_quizzes") {
            SubmittedAnswersScreen(navController = primaryNavController)
        }

        composable("quiz_review") {
            QuizReviewScreen(navController = primaryNavController)
        }

    }


}

fun NavGraphBuilder.quizFlowGraph(navController: NavController) {
    navigation(
        // The first screen to show in this flow
        startDestination = "quiz_overview/{sessionId}",
        // A unique route name for the entire graph. We still pass the quizId here.
        route = "quiz_flow/{sessionId}"
    ) {
        // First screen in the graph
        composable("quiz_overview/{sessionId}") { backStackEntry ->
            // Get the NavController for the PARENT graph
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow/{sessionId}")
            }
            // Use the parent's backStackEntry to get the shared ViewModel
            val viewModel: QuizFlowViewModel = hiltViewModel(parentEntry)
            QuizOverviewScreen(navController = navController, viewModel = viewModel)
        }

        // Second screen in the graph
        composable("quiz_taking") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow/{sessionId}")
            }
            val viewModel: QuizFlowViewModel = hiltViewModel(parentEntry)
            QuizTakingScreen(navController = navController, viewModel = viewModel)
        }
    }
}

fun getHardcodedQuizData(): QuizData {
    // This function mimics fetching and parsing your JSON data.
    return QuizData(
        id = "3f705b7f-bb81-44c8-844e-48964c248ad4",
        title = "USA History Quiz",
        description = "A quiz covering key events and figures in United States history.",
        createdAt = "2025-05-23T13:45:54.907741",
        questions = listOf(
            Question(
                id = "716de252-8a7e-443d-b9c7-be0c981cb045",
                questionText = "Who was the primary author of the Declaration of Independence?",
                time = 60,
                type = "MULTIPLE_CHOICE",
                options = listOf(
                    Option("f6343f60-def1-4219-9db9-53ace4f4d5af", "Benjamin Franklin", false),
                    Option("f77492eb-d4dc-4be9-a94c-1c9a6e1af564", "George Washington", false),
                    Option("5dd4c5b0-2034-4461-b20a-2ebb7a39a691", "Thomas Jefferson", true),
                    Option("31a25818-e03f-4298-9edf-f574c0c7390a", "John Adams", false)
                )
            ),
            Question(
                id = "2402ae5c-5bda-426f-8a24-7b5ae1a4399e",
                questionText = "The American Civil War began in 1861.",
                time = 60,
                type = "TRUE_FALSE",
                options = listOf(
                    Option("fc3c872f-ae6e-4ae5-8c56-0b45bd1409da", "True", true),
                    Option("04b12135-c151-457e-8b28-164c2e0dba26", "False", false)
                )
            )
        )
    )
}