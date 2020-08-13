package com.rocappdev.beerscatalog.repository

import com.rocappdev.beerscatalog.domain.Beer

sealed class BeerResponse

data class BeerList(val list: List<Beer> = listOf()) : BeerResponse()

data class ErrorResponse(val message: String) : BeerResponse()