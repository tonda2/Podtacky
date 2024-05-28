package cz.cvut.fit.podtacky.features.coaster.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import cz.cvut.fit.podtacky.features.coaster.domain.Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoasterLocalDataSource(private val coasterDao: CoasterDao) {

    fun getCoastersStream(): LiveData<List<Coaster>> {
        return coasterDao.getCoasters().map { dbCoasters ->
            dbCoasters.map { dbCoaster ->
                dbCoaster.toDomain()
            }
        }
    }

    suspend fun getCoaster(id: String): Coaster? = coasterDao.getCoasterById(id)?.toDomain()

    fun searchByBrewery(name: String): Flow<List<Coaster>> = coasterDao.searchByBreweryName(name).map { dbCoasters ->
        dbCoasters.map { dbCoaster ->
            dbCoaster.toDomain()
        }
    }

    suspend fun insert(coaster: Coaster) {
        val dbCoaster = DbCoaster(
            brewery = coaster.brewery,
            description = coaster.description,
            dateAdded = coaster.dateAdded,
            city = coaster.city,
            count = coaster.count
        )
        coasterDao.insert(dbCoaster)
    }

    private fun DbCoaster.toDomain(): Coaster {
        return Coaster(
            coasterId = coasterId,
            brewery = brewery,
            description = description,
            dateAdded = dateAdded,
            city = city,
            count = count
        )
    }

    private fun DbLabel.toDomain(): Label {
        return Label(
            labelId = labelId,
            title = title
        )
    }
}