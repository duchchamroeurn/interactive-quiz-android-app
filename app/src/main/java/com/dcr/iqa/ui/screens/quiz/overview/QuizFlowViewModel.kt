package com.dcr.iqa.ui.screens.quiz.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcr.iqa.data.model.response.QuizSessionDetails
import com.dcr.iqa.data.respository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizFlowUiState(
    val isLoading: Boolean = true,
    val quizDetails: QuizSessionDetails? = null,
    val error: String? = null,

    // State for the quiz-taking screen
    val currentQuestionIndex: Int = 0,
    val userAnswers: Map<String, String> = emptyMap(), // Map<QuestionID, SelectedOptionID>
    val quizFinished: Boolean = false,

    val remainingTimeSeconds: Int = 0,
    val totalQuizTimeSeconds: Int = 0,
    val isTimerRunning: Boolean = false
)

@HiltViewModel
class QuizFlowViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    savedStateHandle: SavedStateHandle // Hilt provides this to access navigation arguments
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizFlowUiState())
    val uiState: StateFlow<QuizFlowUiState> = _uiState.asStateFlow()

    private val sessionId: String = checkNotNull(savedStateHandle["sessionId"])
    private var timerJob: Job? = null

    init {
        fetchQuizDetails()
    }

    private fun fetchQuizDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isTimerRunning = false) } // Ensure timer isn't marked as running initially
            val result = quizRepository.getQuizDetails(sessionId)
            result.onSuccess { details ->
                val totalTime = details.quiz.questions.sumOf { it.time }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        quizDetails = details,
                        totalQuizTimeSeconds = totalTime,
                        remainingTimeSeconds = totalTime // Set initial time, but don't start timer yet
                    )
                }
                // Timer will be started explicitly by the QuizTakingScreen
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, error = error.localizedMessage)
                }
            }
        }
    }

    fun selectAnswer(questionId: String, optionId: String) {
        _uiState.update { currentState ->
            currentState.copy(userAnswers = currentState.userAnswers + (questionId to optionId))
        }
    }

    fun nextQuestion() {
        val questionCount = _uiState.value.quizDetails?.quiz?.questions?.size ?: 0
        if (_uiState.value.currentQuestionIndex < questionCount - 1) {
            _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
        }
    }

    fun previousQuestion() {
        if (_uiState.value.currentQuestionIndex > 0) {
            _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex - 1) }
        }
    }

    fun finishQuiz() {
        timerJob?.cancel() // Stop the timer if quiz is finished manually
        // TODO: Here you would submit the answers to your backend
        _uiState.update { it.copy(quizFinished = true, isLoading = false) } // Ensure loading is false
    }
    fun startQuizTimerIfNotRunning() {
        if (_uiState.value.isTimerRunning || _uiState.value.quizDetails == null || _uiState.value.quizFinished) {
            return // Don't start if already running, data not loaded, or quiz finished
        }

        _uiState.update { it.copy(isTimerRunning = true) } // Mark timer as running

        timerJob?.cancel() // Cancel any existing timer just in case
        timerJob = viewModelScope.launch {
            // Ensure remainingTime is set to totalTime if it's a fresh start or reset
            // If you want to resume, you'd use the current _uiState.value.remainingTimeSeconds
            var timeLeft = _uiState.value.totalQuizTimeSeconds
            _uiState.update { it.copy(remainingTimeSeconds = timeLeft) } // Initialize on start

            while (timeLeft > 0 && !_uiState.value.quizFinished) {
                delay(1000L)
                timeLeft--
                _uiState.update { it.copy(remainingTimeSeconds = timeLeft) }
            }
            if (!_uiState.value.quizFinished && timeLeft <= 0) {
                finishQuiz()
            }
        }
    }

//    private fun startQuizTimer(totalTime: Int) {
//        timerJob?.cancel() // Cancel any existing timer
//        timerJob = viewModelScope.launch {
//            var timeLeft = totalTime
//            while (timeLeft > 0 && !_uiState.value.quizFinished) { // Also stop if quiz is manually finished
//                delay(1000L)
//                timeLeft--
//                _uiState.update { it.copy(remainingTimeSeconds = timeLeft) }
//            }
//            // Timer finished or quiz manually finished
//            if (!_uiState.value.quizFinished) { // If timer ran out, finish quiz
//                finishQuiz()
//            }
//        }
//    }
}