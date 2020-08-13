package com.rocappdev.beerscatalog.network

import com.rocappdev.beerscatalog.domain.Beer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.punkapi.com/v2/"

class ApiClient {
    private fun getApiService(): ApiService {
        val retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }

    suspend fun getBeers(page: Int): List<Beer> {
        return getApiService().getBeerList(page)
    }
}