package cz.cvut.fit.podtacky.core.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.cvut.fit.podtacky.features.coaster.presentation.detail.DetailScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.edit.EditScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.large_photo.LargePhotoScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.list.ListScreen
import cz.cvut.fit.podtacky.features.coaster.presentation.search.SearchScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Surface {
        NavHost(
            navController = navController,
            startDestination = Screen.ListScreen.route,
            modifier = modifier,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable(route = Screen.ListScreen.route) {
                ListScreen(navController = navController)
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
            composable(
                route = Screen.LargePhotoScreen.route + "/{id}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.LongType
                    }
                )
            ) {
                LargePhotoScreen(navController = navController)
            }
        }
    }
}