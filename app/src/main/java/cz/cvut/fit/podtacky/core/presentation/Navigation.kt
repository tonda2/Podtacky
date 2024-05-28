package cz.cvut.fit.podtacky.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.fit.podtacky.features.coaster.presentation.add.AddScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.detail.DetailScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.edit.EditScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.search.SearchScreen

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
        composable(route = Screen.AddScreen.route) {
            AddScreen(navController = navController)
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(
            route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.LongType
                }
            )
        ) {
            DetailScreen(navController = navController)
        }
        composable(
            route = Screen.EditScreen.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.LongType
                }
            )
        ) {
            EditScreen(navController = navController)
        }
    }
}