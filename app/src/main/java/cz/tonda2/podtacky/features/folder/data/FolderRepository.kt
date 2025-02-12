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

    fun getSubFolders(parentUid: String): Flow<List<Folder>> {
        return folderDao.getSubFolders(parentUid).map { list ->
            list.map { dbFolder -> dbFolder.toDomain() }
        }
    }

    fun getFoldersWithoutParent(): Flow<List<Folder>> {
        return folderDao.getFoldersWithoutParent().map { list ->
            list.map { dbFolder -> dbFolder.toDomain() }
        }
    }

    suspend fun getFolderByUid(folderUid: String): Folder? {
        return folderDao.getFolderByUid(folderUid)?.toDomain()
    }

    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder.toDb())
    }

    suspend fun deleteFolder(folder: Folder) {
        return folderDao.delete(folder.toDb())
    }

    suspend fun markUploaded(uid: String) {
        folderDao.markUploaded(uid)
    }

    suspend fun markDeleted(uid: String) {
        folderDao.markDeleted(uid)
    }
}

fun DbFolder.toDomain() = Folder(
    folderUid = folderUid,
    name = name,
    parentUid = parentUid,
    uploaded = uploaded,
    deleted = deleted
)

fun Folder.toDb() = DbFolder(
    folderUid = folderUid,
    name = name,
    parentUid = parentUid,
    uploaded = uploaded,
    deleted = deleted
)