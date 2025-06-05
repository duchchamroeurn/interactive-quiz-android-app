package com.dcr.iqa.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcr.iqa.data.model.response.UserResponse
import com.dcr.iqa.data.respository.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager
): ViewModel() {

    val userState: StateFlow<UserResponse?> = MutableStateFlow(userSessionManager.getUser())

    fun signOut() {
        viewModelScope.launch {
            userSessionManager.clearSession()
        }
    }
}