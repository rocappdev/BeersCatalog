package com.rocappdev.beerscatalog.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Beer(
    val id: Int,
    val name: String?,
    val tagline: String?,
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val abv: Double?,
    val ibu: Double?,
    @SerializedName("food_pairing")
    val foodPairing: List<String>?,
    var notAvailable: Boolean
) : Serializable