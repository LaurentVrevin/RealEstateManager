package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.data.model.Estate

interface EstateInterface {
    fun getEstateList(): LiveData<List<Estate>>
    fun addEstate(estate: Estate)
}