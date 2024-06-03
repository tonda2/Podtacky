package cz.cvut.fit.podtacky.features.fact.data.api

import kotlinx.serialization.Serializable

@Serializable
data class FactApiResponse(
    val fact: String
)