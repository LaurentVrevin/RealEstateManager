package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateRepository

class EstateViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    //Get the LiveData object containing the list of estates
    fun getEstateList(): LiveData<List<Estate>> {
        return estateRepository.getEstateList()
    }

    //Add a new estate
    fun addEstate(estate: Estate) {
        estateRepository.addEstate(estate)
    }
}