package cz.tonda2.podtacky.core.presentation

import cz.tonda2.podtacky.features.coaster.domain.Coaster
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType
import java.text.SimpleDateFormat

/**
 * Sorts coasters by provided type. Uses secondary sort.
 */
fun sortCoastersByType(coasters: List<Coaster>, order: CoasterSortType): List<Coaster> {
    val formatter = SimpleDateFormat("dd.MM.yyy")

    return when (order) {
        CoasterSortType.DATE -> coasters.sortedWith(
            compareByDescending<Coaster> { formatter.parse(it.dateAdded) }
                .thenBy { it.brewery.lowercase() }
        )

        CoasterSortType.BREWERY -> coasters.sortedWith(
            compareBy<Coaster> { it.brewery.lowercase() }
                .thenBy { formatter.parse(it.dateAdded) }
        )

        CoasterSortType.COUNT -> coasters.sortedWith(
            compareByDescending<Coaster> { it.count }
                .thenBy { it.brewery.lowercase() }
        )
    }
}
