package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.MainApplication
import com.openclassrooms.realestatemanager.database.EstateDatabase
import com.openclassrooms.realestatemanager.database.PhotoDao
import com.openclassrooms.realestatemanager.database.PropertyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEstateDatabase(@ApplicationContext context: Context): EstateDatabase {
        return Room.databaseBuilder(
            context,
            EstateDatabase::class.java,
            "real_estate_database"
        ).build()
    }

    @Provides
    fun providePropertyDao(estateDatabase: EstateDatabase): PropertyDao {
        return estateDatabase.propertyDao()
    }
    @Provides
    fun providePhotoDao(estateDatabase: EstateDatabase): PhotoDao {
        return estateDatabase.photoDao()
    }
}