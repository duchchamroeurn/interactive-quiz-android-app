package com.dcr.iqa.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcr.iqa.data.model.response.AvailableQuiz
import com.dcr.iqa.data.respository.QuizRepository
import com.dcr.iqa.data.respository.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Represents the state of the HomeScreen UI
data class HomeUiState(
    val isLoading: Boolean = true,
    val username: String = "",
    val availableQuizzes: List<AvailableQuiz> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val sessionManager: UserSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Fetch username from session
            val user = sessionManager.getUser()!!
            val username = user.username
            val userId = user.userId // Get the logged-in user's ID
            // Fetch quizzes from the repository
            val result = quizRepository.getAvailableQuizzesForUser(userId)

            result.onSuccess { quizzes ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        username = username,
                        availableQuizzes = quizzes
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        username = username, // Still show username even if quizzes fail
                        error = error.localizedMessage
                    )
                }
            }
        }
    }
}