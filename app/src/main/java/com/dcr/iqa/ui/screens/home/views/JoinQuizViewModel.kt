package com.dcr.iqa.ui.screens.home.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcr.iqa.data.model.response.QuizSessionDetails
import com.dcr.iqa.data.respository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JoinQuizUiState(
    val isLoading: Boolean = false,
    val joinError: String? = null,
    val joinedQuizDetails: QuizSessionDetails? = null // Holds data on success
)

@HiltViewModel
class JoinQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinQuizUiState())
    val uiState: StateFlow<JoinQuizUiState> = _uiState.asStateFlow()

    fun onJoinQuizClicked(sessionCode: String) {
        if (sessionCode.isBlank()) {
            _uiState.update { it.copy(joinError = "Session code cannot be empty.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, joinError = null, joinedQuizDetails = null) }

        viewModelScope.launch {
            val result = quizRepository.joinQuizBySessionCode(sessionCode.trim().uppercase())
            result.onSuccess { details ->
                _uiState.update {
                    it.copy(isLoading = false, joinedQuizDetails = details)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, joinError = error.message ?: "Failed to join quiz.")
                }
            }
        }
    }

    // Call this after navigation has occurred to prevent re-navigation
    fun onJoinSuccessNavigationConsumed() {
        _uiState.update { it.copy(joinedQuizDetails = null) }
    }
}