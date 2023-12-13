package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.database.PhotoDao
import com.openclassrooms.realestatemanager.database.PropertyDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EstateRepository @Inject constructor(private val propertyDao: PropertyDao, private val photoDao: PhotoDao){


    val propertyListDao: LiveData<List<Property>> = propertyDao.getAllProperties()

    suspend fun addProperty(property: Property) {
        propertyDao.insertProperty(property)
    }

    // Mettre à jour une propriété
    suspend fun updateProperty(property: Property) {
        propertyDao.updateProperty(property)
    }
    fun getPropertyById(propertyId: String): LiveData<Property> {
        return propertyDao.getPropertyById(propertyId)
    }
}