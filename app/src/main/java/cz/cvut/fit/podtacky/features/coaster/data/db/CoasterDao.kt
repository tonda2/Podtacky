package cz.cvut.fit.podtacky.features.coaster.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CoasterDao {

    @Transaction
    @Query("SELECT * FROM coasters ORDER BY dateAdded DESC")
    fun getCoasters(): LiveData<List<DbCoaster>>

    @Query("SELECT * FROM coasters WHERE coasterId = :id")
    suspend fun getCoasterById(id: String): DbCoaster?

    @Query(
        "SELECT * FROM coasters " +
                "WHERE LOWER(brewery) like '%' || LOWER(:query) || '%' OR " +
                "LOWER(description) like '%' || LOWER(:query) || '%'" +
                "ORDER BY dateAdded DESC")
    fun searchCoasters(query: String): Flow<List<DbCoaster>>

    @Insert
    suspend fun insert(coaster: DbCoaster)

    @Delete
    suspend fun delete(coaster: DbCoaster)
}