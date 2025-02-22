package cz.tonda2.podtacky.features.folder.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Insert
    suspend fun insert(folder: DbFolder)

    @Delete
    suspend fun delete(folder: DbFolder)

    @Update
    suspend fun update(folder: DbFolder)

    @Transaction
    @Query("SELECT * FROM folders")
    fun getFolders(): Flow<List<DbFolder>>

    @Transaction
    @Query("SELECT * FROM folders WHERE parentUid = :uid")
    fun getSubFolders(uid: String): Flow<List<DbFolder>>

    @Transaction
    @Query("SELECT * FROM folders WHERE parentUid is NULL")
    fun getFoldersWithoutParent(): Flow<List<DbFolder>>

    @Query("SELECT * FROM folders WHERE folderUid = :uid")
    suspend fun getFolderByUid(uid: String): DbFolder?

    @Query("UPDATE folders SET uploaded = 1 WHERE folderUid = :uid")
    suspend fun markUploaded(uid: String)

    @Query("UPDATE folders SET deleted = 1 WHERE folderUid = :uid")
    suspend fun markDeleted(uid: String)

    @Query("SELECT * FROM folders WHERE LOWER(name) like '%' || LOWER(:query) || '%'")
    fun searchFolders(query: String): Flow<List<DbFolder>>
}