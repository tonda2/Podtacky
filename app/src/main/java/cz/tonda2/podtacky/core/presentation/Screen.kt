package cz.tonda2.podtacky.core.presentation

sealed class Screen(val route: String) {

    data object ListScreen : Screen("list")

    data object SearchScreen : Screen("search")

    data object DetailScreen : Screen("detail") {
        const val ID = "id"
    }

    data object EditScreen : Screen("edit") {
        const val ID = "id"
    }

    data object LargePhotoScreen : Screen("photo") {
        const val ID = "id"
    }

    data object ProfileScreen : Screen("profile")
}