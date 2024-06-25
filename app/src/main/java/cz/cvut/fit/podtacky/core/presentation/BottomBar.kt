package cz.cvut.fit.podtacky.core.presentation

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
import cz.cvut.fit.podtacky.R

@Composable
fun BottomBar(
    navController: NavController,
    isList: Boolean
) {
    BottomAppBar(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
        NavigationBarItem(
            painter = painterResource(id = R.drawable.baseline_list_24),
            name = stringResource(R.string.podt_cky),
            selected = isList,
            onClick = {
                if (!isList) {
                    navController.navigate(Screen.ListScreen.route)
                }
            }
        )

//        NavigationBarItem(
//            painter = painterResource(id = R.drawable.baseline_psychology_24),
//            name = stringResource(R.string.fakta),
//            selected = !isList,
//            onClick = {
//                if (isList) {
//                    navController.navigate(Screen.FactScreen.route)
//                }
//            }
//        )
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
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onTertiary
    }

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(painter = painter, contentDescription = null, tint = contentColor)
        },
        label = {
            Text(text = name, style = MaterialTheme.typography.labelMedium, color = contentColor)
        }
    )
}