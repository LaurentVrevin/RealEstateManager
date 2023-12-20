package com.openclassrooms.realestatemanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val typeOfProperty: String,
    val price: Double,
    val surface: Double,
    val numberOfRooms: String,
    val numberOfBedrooms: String,
    val numberOfBathrooms: String,
    val address: String,
    val city: String,
    val country: String,
    val photos: List<Photo>,
    val isNearSchools: Boolean,
    val isNearRestaurants: Boolean,
    val isNearShops: Boolean,
    val isNearBuses: Boolean,
    val isNearTramway: Boolean,
    val isNearPark: Boolean,
    val latitude: Double,
    val longitude: Double,
    val dateAdded: String,
    val dateSold: String,
    val agentId: String,
    val isSold: Boolean,
    val isFavorite: Boolean
)
