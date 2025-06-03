package com.dcr.iqa.screens.quiz.review.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ReviewedOptionRow(optionText: String, isCorrect: Boolean, isSelected: Boolean) {

    val backgroundColor: Color
    val borderColor: Color
    val icon: ImageVector?
    val iconColor: Color

    when {
        // State 1: User selected the correct answer
        isSelected && isCorrect -> {
            backgroundColor = Color.Green.copy(alpha = 0.1f)
            borderColor = Color.Green
            icon = Icons.Default.Check
            iconColor = Color.Green
        }
        // State 2: User selected an incorrect answer
        isSelected && !isCorrect -> {
            backgroundColor = Color.Red.copy(alpha = 0.1f)
            borderColor = Color.Red
            icon = Icons.Default.Close
            iconColor = Color.Red
        }
        // State 3: The correct answer, which the user did NOT select
        !isSelected && isCorrect -> {
            backgroundColor = Color.Transparent
            borderColor = Color.Green
            icon = Icons.Default.Check
            iconColor = Color.Green
        }
        // State 4: Any other incorrect option
        else -> {
            backgroundColor = Color.Transparent
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            icon = null
            iconColor = Color.Transparent
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = optionText, modifier = Modifier.weight(1f))
            if (icon != null) {
                Icon(
                    imageVector = icon, contentDescription = null, // Decorative
                    tint = iconColor, modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}