package cz.cvut.fit.podtacky.core.presentation

sealed class Screen(val route: String) {

    data object ListScreen : Screen("list")
}