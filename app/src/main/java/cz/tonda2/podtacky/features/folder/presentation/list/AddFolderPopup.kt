package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.tonda2.podtacky.R

@Composable
fun AddFolderPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    textValue: String,
    onTextValueChange: (String) -> Unit,
    textFieldPlaceholder: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.pridat))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.zrusit))
            }
        },
        title = { Text(stringResource(R.string.add_folder)) },
        text = {
            TextField(
                value = textValue,
                onValueChange = onTextValueChange,
                placeholder = {
                    Text(
                        text = textFieldPlaceholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                },
            )
        }
    )
}