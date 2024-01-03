package com.openclassrooms.realestatemanager


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery

import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.data.model.SearchCriteria
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

import org.junit.Assert.assertTrue
import org.junit.Rule
import org.mockito.ArgumentCaptor

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EstateRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: EstateRepository
    private lateinit var propertyDao: PropertyDao
    private var propertyForTest = Property(id = "testId",
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
        isSold = false)

    @Before
    fun setup() {
        propertyDao = mock(PropertyDao::class.java)
        repository = EstateRepository(propertyDao)
    }

    @Test
    fun addProperty_propertyDao() = runBlockingTest {

        repository.addProperty(propertyForTest)

        verify(propertyDao).insertProperty(propertyForTest)
    }

    @Test
    fun updateProperty_propertyDao() = runBlockingTest {
        // Créer une copie de propertyForTest avec un titre mis à jour
        val updatedProperty = propertyForTest.copy(title = "Updated Title")

        // Appeler la méthode de mise à jour avec la copie modifiée
        repository.updateProperty(updatedProperty)

        // Vérifier que la méthode updateProperty du DAO a été appelée avec la propriété mise à jour
        verify(propertyDao).updateProperty(updatedProperty)
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