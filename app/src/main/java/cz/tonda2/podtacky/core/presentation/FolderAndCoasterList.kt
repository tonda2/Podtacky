package cz.tonda2.podtacky.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.folder.domain.Folder

@Composable
fun FolderAndCoasterList(
    folders: List<Folder>,
    coasters: List<Coaster>,
    showFolderOptions: Boolean = true,
    emptyText: String,
    modifier: Modifier,
    onFolderClick: (Folder) -> Unit,
    onCoasterClick: (Coaster) -> Unit,
    onFolderRenameClick: (Folder) -> Unit = {},
    onFolderDeleteClick: (Folder) -> Unit = {}
) {
    if (folders.isEmpty() && coasters.isEmpty()) {
        NoResults(
            text = emptyText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(folders) { folder ->
                FolderCard(
                    folder = folder,
                    showOptions = showFolderOptions,
                    onClick = { onFolderClick(folder) },
                    onRenameClick = { onFolderRenameClick(folder) },
                    onDeleteClick = { onFolderDeleteClick(folder) }
                )
            }
            items(coasters) { coaster ->
                CoasterCard(
                    coaster = coaster
                ) {
                    onCoasterClick(coaster)
                }
            }
        }
    }
}