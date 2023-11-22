package com.openclassrooms.realestatemanager.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.repositories.EstateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EstateViewModel @Inject constructor (
    private val estateRepository: EstateRepository
) : ViewModel() {

    // Method to add
    fun addProperty(property: Property) {
        estateRepository.addProperty(property)
    }

    // Method to get back the List
    fun getPropertyList(): LiveData<List<Property>> {
        return estateRepository.propertyListLiveData
    }

}