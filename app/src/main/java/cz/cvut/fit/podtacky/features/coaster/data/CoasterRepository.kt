package cz.cvut.fit.podtacky.features.coaster.data

import androidx.lifecycle.LiveData
import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.Date
import java.util.Random

class CoasterRepository(
    private val localDataSource: CoasterLocalDataSource
) {

    fun getCoasters(): LiveData<List<Coaster>> {
        return localDataSource.getCoastersStream()
    }

    suspend fun getCoasterById(id: String): Coaster {
        val coaster = localDataSource.getCoaster(id)
        if (coaster != null) return coaster
        throw RuntimeException("Id doesn't exist!")
    }

    fun searchCoasterByBreweryName(name: String): Flow<List<Coaster>> {
        return localDataSource.searchByBrewery(name)
    }

    suspend fun addCoaster(coaster: Coaster) {
        return localDataSource.insert(coaster)
    }
}