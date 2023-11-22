package com.openclassrooms.realestatemanager.data.model

data class Property(
    val id: String,
    val description: String,
    val typeOfProperty: String,
    val price: String,
    val surface: String,
    val numberOfRooms: String,
    val numberOfBedrooms: String,
    val numberOfBathrooms: String,
    val address: String,
    val postalCode: String,
    val country: String,
    val photos: List<Photo>
)
