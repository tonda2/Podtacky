package cz.cvut.fit.podtacky.features.coaster.domain

data class Coaster(
    val coasterId: Long = 0L,
    val brewery: String,
    val description: String,
    val dateAdded: String,
    val city: String,
    val count: Int,
    val frontUri: String,
    val backUri: String
//    val labels: List<Label>
)