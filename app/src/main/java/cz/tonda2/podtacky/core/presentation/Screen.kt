package cz.tonda2.podtacky.core.presentation

sealed class Screen(val route: String) {

    data object ListScreen : Screen("list")

    data object SearchScreen : Screen("search")

    data object DetailScreen : Screen("detail") {
        const val ID = "id"
    }

    data object EditScreen : Screen("edit") {
        const val ID = "id"
        const val FOLDER_ID = "folderId"
    }

    data object LargePhotoScreen : Screen("photo") {
        const val ID = "id"
        const val START_INDEX = "startIndex"
    }

    data object ProfileScreen : Screen("profile")

    data object FolderScreen : Screen("folder") {
        const val ID = "id"
        const val SHOW_ADD_POPUP = "showAdd"
    }
}