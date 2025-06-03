package com.dcr.iqa.ui.screens.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun WelcomeSection(userName: String) {
    Column {
        Text(text = "Hello, 👋", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Welcome $userName!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}