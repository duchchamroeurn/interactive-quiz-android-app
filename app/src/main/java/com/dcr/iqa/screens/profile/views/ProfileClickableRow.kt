package com.dcr.iqa.screens.profile.views

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A full-width clickable row, typically for navigation or actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileClickableRow(
    text: String,
    textColor: Color = Color.Unspecified, // Default to local text color
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text, color = textColor) },
        modifier = Modifier.clickable(onClick = onClick),
        trailingContent = {
            if (textColor != MaterialTheme.colorScheme.error) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}