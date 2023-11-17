package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.data.model.Estate

class EstateRepository : EstateInterface {

    // LiveData to observe changes in the list of estates
    private val estateListLiveData = MutableLiveData<List<Estate>>()

    init {
        // Initialize the list with an empty mutable list
        estateListLiveData.value = mutableListOf()
    }

    //Get the LiveData object containing the list of estates
    override fun getEstateList(): LiveData<List<Estate>> {
        return estateListLiveData
    }

    //Add a new estate to the list
    override fun addEstate(estate: Estate) {

        // Retrieve the current list or create a new mutable list if null
        val currentList = estateListLiveData.value?.toMutableList() ?: mutableListOf()
        // Add the new estate to the list
        currentList.add(estate)
        // Update the LiveData with the modified list
        estateListLiveData.value = currentList
    }
}