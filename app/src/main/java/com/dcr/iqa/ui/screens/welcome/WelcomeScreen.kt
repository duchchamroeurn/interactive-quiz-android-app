package com.dcr.iqa

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dcr.iqa.data.respository.UserPreferencesRepository
import com.dcr.iqa.ui.screens.welcome.WelcomeViewModel
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    // Get the context and a coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPrefsRepository = remember { UserPreferencesRepository(context) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Quiz App!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            viewModel.getStarted()
            navController.navigate("login") {
                popUpTo("welcome") {
                    inclusive = true
                }
            }
        }) {
            Text("Let's Get Started")
        }
    }
}