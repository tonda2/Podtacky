package cz.cvut.fit.podtacky.features.coaster.data

import androidx.lifecycle.LiveData
import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.Flow

class CoasterRepository(
    private val localDataSource: CoasterLocalDataSource
) {

    fun getCoasters(): LiveData<List<Coaster>> {
        return localDataSource.getCoastersStream()
    }

    suspend fun getCoasterById(id: String): Coaster {
        val coaster = localDataSource.getCoaster(id)
        if (coaster != null) return coaster
        throw RuntimeException("Id $id doesn't exist!")
    }

    fun searchCoasters(query: String): Flow<List<Coaster>> {
        return localDataSource.seachCoasters(query)
    }

    suspend fun addCoaster(coaster: Coaster) {
        localDataSource.insert(coaster)
    }

    suspend fun deleteCoaster(coaster: Coaster) {
        return localDataSource.delete(coaster)
    }
}