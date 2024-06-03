package cz.cvut.fit.podtacky.features.fact.data.api

class FactRemoteDataSource(
    private val apiDescription: FactApiDescription
) {

    suspend fun getFact(): String {
        return apiDescription.getFact().fact
    }
}