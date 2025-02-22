package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cz.tonda2.podtacky.R

@Composable
fun BottomBar(
    navController: NavController,
    selectedInd: Int
) {
    BottomAppBar(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
        NavigationBarItem(
            painter = painterResource(id = R.drawable.baseline_list_24),
            name = stringResource(R.string.sbirka),
            selected = selectedInd == BottomBarScreenIndex.LIST.index,
            onClick = {
                if (selectedInd != BottomBarScreenIndex.LIST.index) {
                    navController.navigate(Screen.ListScreen.route)
                }
            }
        )

        NavigationBarItem(
            painter = painterResource(id = R.drawable.baseline_folder_24),
            name = stringResource(R.string.slozky),
            selected = selectedInd == BottomBarScreenIndex.FOLDER.index,
            onClick = {
                if (selectedInd != BottomBarScreenIndex.FOLDER.index) {
                    navController.navigate(Screen.FolderScreen.route + "/-?${Screen.FolderScreen.SHOW_ADD_POPUP}=false")
                }
            }
        )

        NavigationBarItem(
            painter = painterResource(id = R.drawable.baseline_manage_accounts_24),
            name = stringResource(R.string.ucet),
            selected = selectedInd == BottomBarScreenIndex.PROFILE.index,
            onClick = {
                if (selectedInd != BottomBarScreenIndex.PROFILE.index) {
                    navController.navigate(Screen.ProfileScreen.route)
                }
            }
        )
    }
}

@Composable
private fun RowScope.NavigationBarItem(
    painter: Painter,
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondary
    }

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(painter = painter, contentDescription = stringResource(
                R.string.bottom_bar_icon,
                name
            ), tint = contentColor)
        },
        label = {
            Text(text = name, style = MaterialTheme.typography.labelMedium, color = contentColor)
        }
    )
}

enum class BottomBarScreenIndex(
    val index: Int
) {
    LIST(0),
    FOLDER(1),
    PROFILE(2)
}