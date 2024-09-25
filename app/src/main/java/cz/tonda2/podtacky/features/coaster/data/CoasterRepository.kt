package cz.tonda2.podtacky.features.coaster.data

import androidx.lifecycle.LiveData
import cz.tonda2.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.tonda2.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CoasterRepository(
    private val localDataSource: CoasterLocalDataSource
) {

    fun getCoastersLive(): LiveData<List<Coaster>> {
        return localDataSource.getCoastersStream()
    }

    fun getCoastersFlow(): Flow<List<Coaster>> {
        return localDataSource.getCoastersFlow()
    }

    suspend fun getUndeletedCoastersList(): List<Coaster> {
        return localDataSource.getCoastersFlow().first().filter { !it.deleted }
    }

    suspend fun getCoasterById(id: String): Coaster {
        val coaster = localDataSource.getCoaster(id)
        if (coaster != null) return coaster
        throw RuntimeException("Id $id doesn't exist!")
    }

    fun searchCoasters(query: String): Flow<List<Coaster>> {
        return localDataSource.seachCoasters(query)
    }

    suspend fun isCoasterDuplicate(coaster: Coaster): Boolean {
        return localDataSource.isCoasterDuplicate(coaster)
    }

    suspend fun markUploaded(id: String) {
        localDataSource.markUploaded(id)
    }

    suspend fun markDeleted(id: String) {
        localDataSource.markDeleted(id)
    }

    suspend fun addCoaster(coaster: Coaster) {
        localDataSource.insert(coaster)
    }

    suspend fun deleteCoaster(coaster: Coaster) {
        return localDataSource.delete(coaster)
    }
}