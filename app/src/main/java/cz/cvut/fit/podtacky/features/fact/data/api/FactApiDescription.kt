package cz.cvut.fit.podtacky.features.fact.data.api

import retrofit2.http.GET

interface FactApiDescription {

    @GET("fact")
    suspend fun getFact(): FactApiResponse
}