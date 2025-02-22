package cz.tonda2.podtacky.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import cz.tonda2.podtacky.R

@Composable
fun FolderOptionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() }
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.prejmenovat)) },
            leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.rename)) },
            onClick = { onRenameClick() }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.smazat), color = Color.Red) },
            leadingIcon = { Icon(Icons.Outlined.Delete, tint = Color.Red, contentDescription = stringResource(R.string.smazat_slozku)) },
            onClick = { onDeleteClick() }
        )
    }
}