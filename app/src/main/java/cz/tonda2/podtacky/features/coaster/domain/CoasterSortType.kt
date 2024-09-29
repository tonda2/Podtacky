package cz.tonda2.podtacky.features.coaster.domain

enum class CoasterSortType(
    val description: String
) {
    DATE(
        "Podle data přidání"
    ),
    BREWERY(
        "Podle jména pivovaru"
    ),
    COUNT(
        "Podle množství ve sbírce"
    )
}