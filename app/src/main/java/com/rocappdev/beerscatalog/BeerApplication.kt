package com.rocappdev.beerscatalog

import android.app.Application

class BeerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: BeerApplication
    }
}