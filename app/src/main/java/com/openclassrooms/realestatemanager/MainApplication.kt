package com.openclassrooms.realestatemanager

import android.app.Application
import androidx.room.Room
import com.google.android.libraries.places.api.Places
import com.openclassrooms.realestatemanager.database.EstateDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {



    override fun onCreate() {
        super.onCreate()

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }
    }
}