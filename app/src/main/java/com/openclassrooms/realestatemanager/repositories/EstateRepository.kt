package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.data.model.Property
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EstateRepository @Inject constructor(){

    // List livedata of properties
    val propertyListLiveData: MutableLiveData<List<Property>> = MutableLiveData()

    // Add a property to the list
    fun addProperty(property: Property) {
        val currentList = propertyListLiveData.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { add(property) }
        propertyListLiveData.value = updatedList
    }
}