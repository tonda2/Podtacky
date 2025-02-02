package cz.tonda2.podtacky.features.folder.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Insert
    suspend fun insert(folder: DbFolder)

    @Delete
    suspend fun delete(folder: DbFolder)

    @Transaction
    @Query("SELECT * FROM folders")
    fun getFolders(): Flow<List<DbFolder>>

    @Transaction
    @Query("SELECT * FROM folders WHERE parentId = :id")
    fun getSubFolders(id: String): Flow<List<DbFolder>>

    @Transaction
    @Query("SELECT * FROM folders WHERE parentId is NULL")
    fun getFoldersWithoutParent(): Flow<List<DbFolder>>

    @Query("SELECT * FROM folders WHERE folderId = :id")
    suspend fun getFolderById(id: String): DbFolder?

    @Query("UPDATE folders SET uploaded = 1 WHERE folderId = :id")
    suspend fun markUploaded(id: String)

    @Query("UPDATE folders SET deleted = 1 WHERE folderId = :id")
    suspend fun markDeleted(id: String)

}