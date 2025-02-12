package cz.tonda2.podtacky.core.presentation

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
import cz.tonda2.podtacky.features.coaster.presentation.detail.DetailScreen
import cz.tonda2.podtacky.features.coaster.presentation.edit.EditScreen
import cz.tonda2.podtacky.features.coaster.presentation.large_photo.LargePhotoScreen
import cz.tonda2.podtacky.features.coaster.presentation.list.ListScreen
import cz.tonda2.podtacky.features.coaster.presentation.search.SearchScreen
import cz.tonda2.podtacky.features.folder.presentation.list.FolderListScreen
import cz.tonda2.podtacky.features.profile.presentation.ProfileScreen

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
                route = Screen.EditScreen.route + "/{id}?${Screen.EditScreen.FOLDER_UID}={folderUid}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.LongType
                    },
                    navArgument(name = Screen.EditScreen.FOLDER_UID) {
                        type = NavType.StringType
                    }
                )
            ) {
                EditScreen(navController = navController)
            }
            composable(
                route = Screen.LargePhotoScreen.route + "/{id}?${Screen.LargePhotoScreen.START_INDEX}={startIndex}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.LongType
                    },
                    navArgument(name = Screen.LargePhotoScreen.START_INDEX) {
                        type = NavType.IntType
                    }
                )
            ) {
                LargePhotoScreen(navController = navController)
            }
            composable(route = Screen.ProfileScreen.route) {
                ProfileScreen(navController = navController)
            }
            composable(
                route = Screen.FolderScreen.route + "/{uid}?${Screen.FolderScreen.SHOW_ADD_POPUP}={${Screen.FolderScreen.SHOW_ADD_POPUP}}",
                arguments = listOf(
                    navArgument(name = Screen.FolderScreen.UID) {
                        type = NavType.StringType
                    },
                    navArgument(name = Screen.FolderScreen.SHOW_ADD_POPUP) {
                        type = NavType.BoolType
                    }
                )
            ) {
                FolderListScreen(navController = navController, defaultShowAddPopup = it.arguments?.getBoolean(Screen.FolderScreen.SHOW_ADD_POPUP) ?: false)
            }
        }
    }
}