package cz.cvut.fit.podtacky.features.coaster.domain

import java.util.Date

data class Coaster(
    val brewery: String,
    val description: String,
    val dateAdded: Date,
    val city: String,
    val count: Int,
//    val labels: List<Label>
)