package cz.cvut.fit.podtacky.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ListScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.ListScreen.route) {
            ListScreen(navController = navController)
        }
    }
}