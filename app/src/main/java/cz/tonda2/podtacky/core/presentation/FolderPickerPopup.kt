package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.tonda2.podtacky.R
import cz.tonda2.podtacky.features.folder.domain.Folder

@Composable
fun FolderPickerPopup(
    title: String?,
    folders: List<Folder>,
    showBackArrow: Boolean,
    onBackClick: () -> Unit,
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
        title = {
            Row {
                if (showBackArrow) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = title ?: stringResource(R.string.vyberte_slozku))
                }
            }
        },
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
