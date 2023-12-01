package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.data.model.Property

interface EstateItemClickListener {
    fun onEstateItemClick(property: Property)
}