package com.openclassrooms.realestatemanager.data.model

data class SearchCriteria(
    val city:String?,
    val typeOfProperty:String?,
    val minSurface: Double?,
    val maxSurface: Double?,
    val minPrice: Double?,
    val maxPrice: Double?,
    val nearSchools: Boolean,
    val nearRestaurants: Boolean,
    val nearShops: Boolean,
    val nearBuses: Boolean,
    val nearTram: Boolean,
    val nearPark: Boolean,
    val isSold: Boolean =false
)
