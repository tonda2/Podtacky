package cz.tonda2.podtacky.features.folder.data

import cz.tonda2.podtacky.features.folder.data.db.DbFolder
import cz.tonda2.podtacky.features.folder.data.db.FolderDao
import cz.tonda2.podtacky.features.folder.domain.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderRepository(
    private val folderDao: FolderDao
) {

    fun getAllFolders(): Flow<List<Folder>> {
        return folderDao.getFolders().map { list ->
            list.map { dbFolder -> dbFolder.toDomain() }
        }
    }

    suspend fun getFolderById(folderId: String): Folder? {
        return folderDao.getFolderById(folderId)?.toDomain()
    }

    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder.toDb())
    }

    suspend fun deleteFolder(folder: Folder) {
        return folderDao.delete(folder.toDb())
    }

    suspend fun markUploaded(id: String) {
        folderDao.markUploaded(id)
    }

    suspend fun markDeleted(id: String) {
        folderDao.markDeleted(id)
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