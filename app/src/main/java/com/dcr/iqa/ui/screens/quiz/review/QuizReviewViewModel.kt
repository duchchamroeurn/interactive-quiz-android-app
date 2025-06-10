package com.dcr.iqa.ui.screens.quiz.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcr.iqa.data.model.response.QuizReviewData
import com.dcr.iqa.data.respository.QuizRepository
import com.dcr.iqa.data.respository.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizReviewUiState(
    val isLoading: Boolean = true,
    val reviewData: QuizReviewData? = null,
    val error: String? = null
)

@HiltViewModel
class QuizReviewViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val sessionManager: UserSessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizReviewUiState())
    val uiState: StateFlow<QuizReviewUiState> = _uiState.asStateFlow()

    private val sessionId: String = checkNotNull(savedStateHandle["sessionId"])

    init {
        fetchReviewDetails()
    }

    private fun fetchReviewDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val userId = sessionManager.getUser()?.userId

            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, error = "User not logged in.") }
                return@launch
            }
            val result = quizRepository.getQuizReviewDetails(sessionId, userId)
            result.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, reviewData = data) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.localizedMessage) }
            }
        }
    }
}