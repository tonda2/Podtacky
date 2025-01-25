package cz.tonda2.podtacky.core.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.tonda2.podtacky.R

@Composable
fun DeleteConfirmation(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.smazat))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.zrusit))
            }
        },
        title = { Text(text = stringResource(R.string.potvrdte_title)) },
        text = { Text(stringResource(R.string.potvrzeni)) }
    )
}