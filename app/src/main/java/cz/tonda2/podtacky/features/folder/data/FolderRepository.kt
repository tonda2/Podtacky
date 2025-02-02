package cz.tonda2.podtacky.features.folder.data

import cz.tonda2.podtacky.features.folder.data.db.DbFolder
import cz.tonda2.podtacky.features.folder.data.db.FolderDao
import cz.tonda2.podtacky.features.folder.domain.Folder

class FolderRepository(
    private val folderDao: FolderDao
) {

    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder.toDb())
    }
}

fun DbFolder.toDomain() = Folder(
    folderId = folderId,
    name = name,
    parentId = parentId,
    uploaded = uploaded,
    deleted = deleted
)

fun Folder.toDb() = DbFolder(
    folderId = folderId,
    name = name,
    parentId = parentId,
    uploaded = uploaded,
    deleted = deleted
)