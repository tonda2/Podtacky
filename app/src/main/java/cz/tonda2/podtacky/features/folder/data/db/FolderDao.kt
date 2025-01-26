package cz.tonda2.podtacky.features.folder.data.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface FolderDao {

    @Insert
    suspend fun insert(folder: DbFolder)
}