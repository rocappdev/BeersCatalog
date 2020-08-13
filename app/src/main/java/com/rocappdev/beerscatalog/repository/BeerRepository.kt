package com.rocappdev.beerscatalog.repository

import com.rocappdev.beerscatalog.domain.Beer
import com.rocappdev.beerscatalog.network.ApiClient
import com.rocappdev.beerscatalog.util.NetworkUtil
import retrofit2.HttpException

class BeerRepository {
    private val apiClient = ApiClient()

    suspend fun getBeers(page: Int): BeerResponse {
        val response: List<Beer>

        try {
            response = apiClient.getBeers(page)
            updateAvailability(response)
        } catch (e: Exception) {
            return getError(e)
        }
        return BeerList(response)
    }

    private fun getError(e: Exception): ErrorResponse {
        val networkUtil = NetworkUtil()
        var message = "Unexpected error"

        if (e is HttpException)
            message = e.message()
        else if (networkUtil.checkNetwork().not())
            message = "No internet connection"

        return ErrorResponse(message)
    }

    private fun updateAvailability(response: List<Beer>) {
        var beer: Beer?
        Preferences.getBeersNotAvailable()?.forEach { id ->
            beer = response.find { beer ->
                beer.id == id.toInt()
            }
            beer?.let {
                it.notAvailable = true
            }
        }
    }

    fun updateBeerAvailability(beer: Beer, response: List<Beer>): BeerList {
        val aList = ArrayList<Beer>(response)

        val index = response.indexOfFirst {
            it.id == beer.id
        }
        if (index >= 0) {
            aList[index] = beer
        }

        return BeerList(aList)
    }

    fun setBeerAvailability(beer: Beer) {
        val listNotAvailable = Preferences.getBeersNotAvailable()
        listNotAvailable?.let {
            val mListNotAvailable = HashSet<String>(listNotAvailable)
            if (beer.notAvailable) {
                mListNotAvailable.add(beer.id.toString())
                Preferences.setBeersNotAvailable(mListNotAvailable)
            } else {
                mListNotAvailable.remove(beer.id.toString())
                Preferences.setBeersNotAvailable(mListNotAvailable)
            }
        }
    }
}