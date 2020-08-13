package com.rocappdev.beerscatalog.network

import com.rocappdev.beerscatalog.domain.Beer
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("beers")
    suspend fun getBeerList(@Query("page") page: Int): List<Beer>

}