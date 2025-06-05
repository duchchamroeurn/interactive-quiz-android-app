package com.dcr.iqa.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dcr.iqa.data.model.response.UserResponse
import com.dcr.iqa.ui.screens.profile.views.ProfileClickableRow
import com.dcr.iqa.ui.screens.profile.views.ProfileInfoRow
import com.dcr.iqa.ui.screens.profile.views.SectionHeader
import androidx.compose.runtime.getValue

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user: UserResponse? by viewModel.userState.collectAsState()
    // In a real app, you would get the user data from a ViewModel
    val userName = user?.username ?: "Guest"
    val userEmail = user?.email ?: "No email"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Make the screen scrollable
            .systemBarsPadding() // Add padding for status/navigation bars
    ) {
        // --- Account Section ---
        SectionHeader("Account")

        // Username Row
        ProfileInfoRow(label = "Username", value = userName)
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // Email Row
        ProfileInfoRow(label = "Email", value = userEmail)
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // Submitted Answers Row
        ProfileClickableRow(
            text = "Submitted answers", onClick = { navController.navigate("submitted_quizzes") })

        Spacer(modifier = Modifier.height(24.dp))

        // --- Setting Section ---
        SectionHeader("Setting")
        ProfileClickableRow(
            text = "Sign out",
            textColor = MaterialTheme.colorScheme.error, // Red color for emphasis
            onClick = {
                viewModel.signOut()

                // Navigate back to login and clear the entire back stack
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            })
    }
}