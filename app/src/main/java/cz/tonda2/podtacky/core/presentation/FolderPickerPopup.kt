package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.features.folder.domain.Folder

@Composable
fun FolderPickerPopup(
    folders: List<Folder>,
    onItemClick: (Folder) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.ulozit_sem))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.zrusit))
            }
        },
        title = { Text(text = stringResource(R.string.vyberte_slozku)) },
        text = {
            FolderList(
                folders = folders,
                emptyText = stringResource(R.string.folder_picker_empty),
                modifier = Modifier
                    .fillMaxSize(),
                onItemClick = onItemClick
            )
        }
    )
}
