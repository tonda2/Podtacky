package cz.cvut.fit.podtacky.features.coaster.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CoasterDao {

    @Transaction
    @Query("SELECT * FROM coasters")
    fun getCoastersWithLabelsStream(): Flow<List<DbCoaster>>

    @Insert
    suspend fun insert(coaster: DbCoaster)
}