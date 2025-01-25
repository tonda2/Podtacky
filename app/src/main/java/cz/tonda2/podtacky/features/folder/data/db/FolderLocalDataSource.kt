package cz.tonda2.podtacky.features.folder.data.db

import cz.tonda2.podtacky.features.folder.domain.Folder

class FolderLocalDataSource(private val folderDao: FolderDao) {

    suspend fun insert(folder: Folder) {
        val dbFolder = DbFolder(
            name = folder.name,
            parentId = folder.parentId,
            uploaded = folder.uploaded,
            deleted = folder.deleted
        )
        folderDao.insert(dbFolder)
    }
}