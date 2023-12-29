package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.data.model.SearchCriteria
import com.openclassrooms.realestatemanager.database.PropertyDao

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EstateRepository @Inject constructor(private val propertyDao: PropertyDao){


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



    fun searchProperties(criteria: SearchCriteria): LiveData<List<Property>> {
        val queryStringBuilder = StringBuilder("SELECT * FROM properties")
        val args = mutableListOf<Any>()

        val whereClauses = mutableListOf<String>()

        criteria.city?.let {
            whereClauses.add("city LIKE ?")
            args.add("%$it%")
        }
        criteria.typeOfProperty?.let {
            whereClauses.add("typeOfProperty LIKE ?")
            args.add("%$it%")
        }
        criteria.minPrice?.let {
            whereClauses.add("price >= ?")
            args.add(it)
        }
        criteria.maxPrice?.let {
            whereClauses.add("price <= ?")
            args.add(it)
        }
        criteria.minSurface?.let {
            whereClauses.add("surface >= ?")
            args.add(it)
        }
        criteria.maxSurface?.let {
            whereClauses.add("surface <= ?")
            args.add(it)
        }
        if (criteria.nearSchools) {
            whereClauses.add("nearSchools = 1")
        }
        if (criteria.nearRestaurants) {
            whereClauses.add("nearRestaurants = 1")
        }
        if (criteria.nearShops) {
            whereClauses.add("nearShops = 1")
        }
        if (criteria.nearBuses) {
            whereClauses.add("nearBuses = 1")
        }
        if (criteria.nearTram) {
            whereClauses.add("nearTram = 1")
        }
        if (criteria.nearPark) {
            whereClauses.add("nearPark = 1")
        }
        if (criteria.isSold) {
            whereClauses.add("isSold = 1")
        }

        if (whereClauses.isNotEmpty()) {
            queryStringBuilder.append(" WHERE ")
            queryStringBuilder.append(whereClauses.joinToString(" AND "))
        }

        val query = SimpleSQLiteQuery(queryStringBuilder.toString(), args.toTypedArray())
        return propertyDao.searchProperties(query)
    }


}