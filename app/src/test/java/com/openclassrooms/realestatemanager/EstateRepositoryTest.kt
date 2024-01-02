package com.openclassrooms.realestatemanager


import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.database.PropertyDao
import com.openclassrooms.realestatemanager.repositories.EstateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.runBlockingTest


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EstateRepositoryTest {


    private lateinit var repository: EstateRepository
    private lateinit var propertyDao: PropertyDao

    @Before
    fun setup() {
        propertyDao = mock(PropertyDao::class.java)
        repository = EstateRepository(propertyDao)
    }

    @Test
    fun addProperty_propertyDao() = runBlockingTest {
        val property = Property(
            id = "testId",
            title = "Test Title",
            description = "Test Description",
            typeOfProperty = "House",
            dollarsPrice = 100000.0,
            eurosPrice = 81200.0,
            surface = 100.0,
            numberOfRooms = "5",
            numberOfBedrooms = "3",
            numberOfBathrooms = "2",
            address = "123 Test Street",
            city = "Test City",
            country = "Test Country",
            photos = emptyList(),
            isNearSchools = true,
            isNearRestaurants = false,
            isNearShops = true,
            isNearBuses = false,
            isNearTramway = true,
            isNearPark = true,
            latitude = 40.7128,
            longitude = -74.0060,
            dateAdded = "2023-01-01",
            dateSold = "",
            agentId = "agent1",
            isSold = false
        )

        repository.addProperty(property)

        verify(propertyDao).insertProperty(property)
    }

    @Test
    fun updateProperty_propertyDao() = runBlockingTest {
        // Créer une instance de Property qui représente la propriété à modifier
        val property = Property(
            id = "testId",
            title = "Updated Title",
            description = "Updated Description",
            typeOfProperty = "House",
            dollarsPrice = 100000.0,
            eurosPrice = 81200.0,
            surface = 100.0,
            numberOfRooms = "5",
            numberOfBedrooms = "3",
            numberOfBathrooms = "2",
            address = "123 Test Street",
            city = "Test City",
            country = "Test Country",
            photos = emptyList(),
            isNearSchools = true,
            isNearRestaurants = false,
            isNearShops = true,
            isNearBuses = false,
            isNearTramway = true,
            isNearPark = true,
            latitude = 49.182863,
            longitude = -0.370679,
            dateAdded = "2023-01-01",
            dateSold = "",
            agentId = "agent1",
            isSold = false
        )

        // Appeler la méthode de mise à jour
        repository.updateProperty(property)

        // Vérifier que la méthode updateProperty du DAO a été appelée avec la propriété mise à jour
        verify(propertyDao).updateProperty(property)
    }
    @Test
    fun getAllProperties_callsDaoMethod() = runBlockingTest {
        // Appeler la méthode du repository
        repository.propertyListDao

        // Vérifier que la méthode getAllProperties du DAO a été appelée
        verify(propertyDao).getAllProperties()
    }

    @Test
    fun getPropertyById_callsDaoMethod() = runBlockingTest {
        val propertyId = "testId"

        // Appeler la méthode du repository
        repository.getPropertyById(propertyId)

        // Vérifier que la méthode getPropertyById du DAO a été appelée avec le bon ID
        verify(propertyDao).getPropertyById(propertyId)
    }


}