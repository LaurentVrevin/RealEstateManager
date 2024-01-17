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

        // Make a copy of propertyForTest with  a updated title
        val updatedProperty = propertyForTest.copy(title = "Updated Title")


        // Call the method of update with copie edited
        repository.updateProperty(updatedProperty)


        // Check if method updateProperty of dao has been called with updated property
        verify(propertyDao).updateProperty(updatedProperty)
    }
    @Test
    fun getAllProperties_callsDaoMethod() = runBlockingTest {
        // call method of repo
        repository.propertyListDao

        // Check that method getAllPropertie of Dao has been called
        verify(propertyDao).getAllProperties()
    }

    @Test
    fun getPropertyById_callsDaoMethod() = runBlockingTest {
        val propertyId = "testId"

        // call method of repository
        repository.getPropertyById(propertyId)

        // Check that method getPropertyById of Dao has been called with the good id
        verify(propertyDao).getPropertyById(propertyId)
    }


}