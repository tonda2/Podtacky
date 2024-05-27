package cz.cvut.fit.podtacky.features.coaster.data

import cz.cvut.fit.podtacky.features.coaster.data.db.CoasterLocalDataSource
import cz.cvut.fit.podtacky.features.coaster.domain.Coaster
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.Date
import java.util.Random

class CoasterRepository(
    private val localDataSource: CoasterLocalDataSource
) {

    suspend fun getCoasters(): List<Coaster> {
        return localDataSource.getCoastersStream().first()
    }

    suspend fun addCoaster() {
        return localDataSource.insert(
            Coaster(
                brewery = "Plzen",
                description = "Podtacek",
                dateAdded = Date(System.currentTimeMillis()),
                city = "Praha",
                count = 10,
            )
        )
    }
}