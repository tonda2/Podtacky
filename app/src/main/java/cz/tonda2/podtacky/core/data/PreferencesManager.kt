package cz.tonda2.podtacky.core.data

import android.content.Context
import android.content.SharedPreferences
import cz.tonda2.podtacky.features.coaster.domain.CoasterSortType

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val SORT_ORDER_KEY = "sort_order"

    fun saveSortOrder(sortType: CoasterSortType) {
        sharedPreferences.edit().putString(SORT_ORDER_KEY, sortType.name).apply()
    }

    fun getSortOrder(): CoasterSortType {
        val sortOrder = sharedPreferences.getString(SORT_ORDER_KEY, CoasterSortType.BREWERY.name)
        return CoasterSortType.valueOf(sortOrder ?: CoasterSortType.BREWERY.name)
    }
}