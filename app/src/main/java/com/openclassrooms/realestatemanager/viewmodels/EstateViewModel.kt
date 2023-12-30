package com.openclassrooms.realestatemanager.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.data.model.SearchCriteria
import com.openclassrooms.realestatemanager.repositories.EstateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Currency
import javax.inject.Inject


@HiltViewModel
class EstateViewModel @Inject constructor (private val estateRepository: EstateRepository
) : ViewModel() {

    // LiveData list of property
    val propertyList: LiveData<List<Property>> = estateRepository.propertyListDao
    private val _currentCurrency = MutableLiveData(Currency.USD)
    val currentCurrency: LiveData<Currency> = _currentCurrency
    // LiveData to stock data from research
    val searchResults = MutableLiveData<List<Property>?>()


    fun addPropertyDao(property: Property) {
        viewModelScope.launch {
            estateRepository.addProperty(property)
        }
    }

    fun updateProperty(property: Property) {
        viewModelScope.launch {
            estateRepository.updateProperty(property)
        }
    }


    private val _selectedPropertyId = MutableLiveData<String>()
    val selectedProperty: LiveData<Property> = MediatorLiveData<Property>().apply {
        addSource(_selectedPropertyId) { id ->
            estateRepository.getPropertyById(id).observeForever { property ->
                this.value = property
            }
        }
    }


    fun setSelectedPropertyId(propertyId: String) {
        _selectedPropertyId.value = propertyId
    }

    fun performSearch(criteria: SearchCriteria) {
        viewModelScope.launch {
            val results = estateRepository.searchProperties(criteria)
            results.observeForever { searchResults.postValue(it) }
        }
    }

    //Function to change currency
    fun toggleCurrency() {
        if (_currentCurrency.value == Currency.USD) {
            _currentCurrency.value = Currency.EUR
        } else {
            _currentCurrency.value = Currency.USD
        }
    }
    enum class Currency {
        USD, EUR
    }

}