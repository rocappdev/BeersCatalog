package com.rocappdev.beerscatalog.repository

import android.content.SharedPreferences
import com.rocappdev.beerscatalog.BeerApplication

object Preferences {
    private const val BEERS_NOT_AVAILABLE = "beersNotAvailables"
    private val prefs: SharedPreferences =
        BeerApplication.instance.getSharedPreferences(BeerApplication.instance.packageName, 0)

    fun setBeersNotAvailable(list: MutableSet<String>?) {
        list?.let {
            val mList = HashSet<String>(list)
            prefs.edit().putStringSet(
                BEERS_NOT_AVAILABLE, mList).apply()
        }
    }

    fun getBeersNotAvailable(): MutableSet<String>? {
        return prefs.getStringSet(
            BEERS_NOT_AVAILABLE, setOf())
    }
}