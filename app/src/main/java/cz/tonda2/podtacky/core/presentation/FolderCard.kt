package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.features.folder.domain.Folder

@Composable
fun FolderCard(
    folder: Folder,
    onClick: () -> Unit,
    showOptions: Boolean = true,
    onRenameClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        FolderItem(
            folder = folder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .semantics {
                    contentDescription = context.getString(
                        R.string.a_single_folder_description,
                        folder.name
                    )
                },
            showOptions = showOptions,
            menuExpanded = menuExpanded,
            toggleMenu = { menuExpanded = !menuExpanded },
            onRenameClick = onRenameClick,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
private fun FolderItem(
    folder: Folder,
    modifier: Modifier,
    showOptions: Boolean,
    menuExpanded: Boolean,
    toggleMenu: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = folder.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (showOptions) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                onClick = { toggleMenu() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_three_dots),
                    contentDescription = stringResource(R.string.folder_options)
                )
                FolderOptionMenu(
                    expanded = menuExpanded,
                    onDismiss = { toggleMenu() },
                    onRenameClick = { onRenameClick() },
                    onDeleteClick = { onDeleteClick() }
                )
            }
        }
    }
}
