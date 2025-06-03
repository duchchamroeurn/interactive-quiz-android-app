import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dcr.iqa.data.Quiz
import com.dcr.iqa.data.TabBarItem
import com.dcr.iqa.screens.main.views.QuizTopAppBar
import com.dcr.iqa.screens.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun MainScreen(mainNavController: NavController, quizzes: List<Quiz>) {
    val navController = rememberNavController()

    // List of tabs (no changes here)
    val tabBarItems = listOf(
        TabBarItem("Home", Icons.Filled.Home, Icons.Default.Home, "home"),
        TabBarItem("Profile", Icons.Filled.Person, Icons.Default.Person, "profile"),
    )

    // Get current back stack entry to determine the route
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Find the current tab to get its title
    val currentTab = tabBarItems.find { it.route == currentRoute }

    Scaffold(
        topBar = { QuizTopAppBar(title = currentTab?.title) },
        // The Bottom Bar remains the same
        bottomBar = {
            NavigationBar {
                tabBarItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(navController = mainNavController, quizzes = quizzes) }
            composable("profile") { ProfileScreen(mainNavController) }
        }
    }
}