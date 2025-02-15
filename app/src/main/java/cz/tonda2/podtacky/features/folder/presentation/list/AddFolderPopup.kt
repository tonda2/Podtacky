package cz.tonda2.podtacky.features.folder.presentation.list

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.tonda2.podtacky.R

@Composable
fun AddFolderPopup(
    title: String = stringResource(R.string.add_folder),
    confirmButtonText: String = stringResource(R.string.pridat),
    dismissButtonText: String = stringResource(R.string.zrusit),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    textValue: String,
    onTextValueChange: (String) -> Unit,
    textFieldPlaceholder: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm, enabled = textValue.isNotEmpty()) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = dismissButtonText)
            }
        },
        title = { Text(text = title) },
        text = {
            TextField(
                value = textValue,
                onValueChange = onTextValueChange,
                singleLine = true,
                placeholder = {
                    Text(
                        text = textFieldPlaceholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }
    )
}