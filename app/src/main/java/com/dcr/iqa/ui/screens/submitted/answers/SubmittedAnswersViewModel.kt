package com.dcr.iqa.ui.screens.submitted.answers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcr.iqa.data.model.response.QuizSubmissionSummary
import com.dcr.iqa.data.respository.QuizRepository
import com.dcr.iqa.data.respository.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubmissionsUiState(
    val isLoading: Boolean = true,
    val submissions: List<QuizSubmissionSummary> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SubmittedAnswersViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val sessionManager: UserSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmissionsUiState())
    val uiState: StateFlow<SubmissionsUiState> = _uiState.asStateFlow()

    init {
        loadSubmissions()
    }

    private fun loadSubmissions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = sessionManager.getUser()?.userId

            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, error = "User not logged in.") }
                return@launch
            }

            val result = quizRepository.getMySubmissions(userId)
            result.onSuccess { submissionList ->
                _uiState.update {
                    it.copy(isLoading = false, submissions = submissionList)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, error = error.localizedMessage)
                }
            }
        }
    }
}