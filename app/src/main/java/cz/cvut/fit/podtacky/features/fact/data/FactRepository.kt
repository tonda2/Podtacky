package cz.cvut.fit.podtacky.features.fact.data

import cz.cvut.fit.podtacky.features.fact.data.api.FactRemoteDataSource

class FactRepository(
    private val remoteDataSource: FactRemoteDataSource
) {

    companion object {
        private val errorMessage = "API not available!"
    }

    suspend fun getFact(): String {
        return try {
            val fact = remoteDataSource.getFact()
            fact.ifEmpty { errorMessage }
        } catch (_: Throwable) {
            errorMessage
        }
    }
}