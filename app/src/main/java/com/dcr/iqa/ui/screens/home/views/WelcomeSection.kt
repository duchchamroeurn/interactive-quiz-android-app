package com.dcr.iqa.ui.screens.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeSection(userName: String, onJoinQuizClicked: () -> Unit) {
    Column {
        Text(text = "Hello, 👋", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Welcome $userName!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onJoinQuizClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.GroupAdd, contentDescription = "Join Quiz", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("Join a Live Quiz")
        }
    }
}