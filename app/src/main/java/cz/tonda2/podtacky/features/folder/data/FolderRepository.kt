package cz.tonda2.podtacky.features.folder.data

import cz.tonda2.podtacky.features.folder.data.db.FolderLocalDataSource
import cz.tonda2.podtacky.features.folder.domain.Folder

class FolderRepository(
    private val localDataSource: FolderLocalDataSource
) {

    suspend fun addFolder(folder: Folder) {
        localDataSource.insert(folder)
    }
}