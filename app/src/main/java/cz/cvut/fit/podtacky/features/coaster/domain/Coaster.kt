package cz.cvut.fit.podtacky.features.coaster.domain

import java.util.Date

data class Coaster(
    val coasterId: Long = 0L,
    val brewery: String,
    val description: String,
    val dateAdded: String,
    val city: String,
    val count: Int,
//    val labels: List<Label>
)