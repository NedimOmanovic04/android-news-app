package etf.ri.rma.newsfeedapp.screen

import FilterScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { NewsFeedScreen(navController) }
        composable("filters") { FilterScreen(navController) }
        composable("details/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetailsScreen(navController, newsId)
        }
    }
}
