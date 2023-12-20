package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.checkbox.MaterialCheckBox
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.SearchCriteria
import com.openclassrooms.realestatemanager.helper.PropertyTypeAdapterHelper
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel


@SuppressLint("UseSwitchCompatOrMaterialCode")
class PropertySearchDialogFragment : DialogFragment() {

    private lateinit var searchButton: Button
    private lateinit var cityEditText: EditText
    private lateinit var typeOfPropertyEditText: AutoCompleteTextView
    private lateinit var minSurfaceEditText: EditText
    private lateinit var maxSurfaceEditText: EditText
    private lateinit var minPriceEditText: EditText
    private lateinit var maxPriceEditText: EditText
    private lateinit var nearSchoolsCheckBox: MaterialCheckBox
    private lateinit var nearRestaurantsCheckBox: MaterialCheckBox
    private lateinit var nearShopsCheckBox: MaterialCheckBox
    private lateinit var nearBusesCheckBox: MaterialCheckBox
    private lateinit var nearTramwayCheckBox: MaterialCheckBox
    private lateinit var nearParkCheckBox: MaterialCheckBox
    private lateinit var isSoldSwitch: Switch

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_property_search_dialog, container, false)

        searchButton = view.findViewById(R.id.fragment_property_search_dialog_search_button)
        cityEditText = view.findViewById(R.id.cardview_search_where_city_edittext)
        typeOfPropertyEditText = view.findViewById(R.id.cardview_search_type_of_property_edittext_autocomplete)
        minSurfaceEditText = view.findViewById(R.id.cardview_search_surface_minimum_edit)
        maxSurfaceEditText = view.findViewById(R.id.cardview_search_surface_maximum_edit)
        minPriceEditText = view.findViewById(R.id.cardview_search_price_minimum_edittext)
        maxPriceEditText = view.findViewById(R.id.cardview_search_price_maximum_edittext)
        nearSchoolsCheckBox = view.findViewById(R.id.cardview_search_near_schools_checkbox)
        nearRestaurantsCheckBox = view.findViewById(R.id.cardview_search_near_restaurants_checkbox)
        nearShopsCheckBox = view.findViewById(R.id.cardview_search_near_shops_checkbox)
        nearBusesCheckBox = view.findViewById(R.id.cardview_search_near_buses_checkbox)
        nearTramwayCheckBox = view.findViewById(R.id.cardview_search_near_tramway_checkbox)
        nearParkCheckBox = view.findViewById(R.id.cardview_search_near_park_checkbox)
        isSoldSwitch=view.findViewById(R.id.cardview_search_issold_switch)

        setupSearchButtonListener()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PropertyTypeAdapterHelper.createAdapter(requireContext(), typeOfPropertyEditText)


    }
    private fun setupSearchButtonListener() {
        searchButton.setOnClickListener {
            // Logique pour le bouton de recherche
            performSearch()
        }

    }
    private fun performSearch() {
        //Link between data of dialog fragment and SearchCriteria data model
        val criteria = SearchCriteria(
            city = cityEditText.text.toString().ifEmpty { null },
            typeOfProperty = typeOfPropertyEditText.text.toString().ifEmpty { null },
            minPrice = minPriceEditText.text.toString().toDoubleOrNull(),
            maxPrice = maxPriceEditText.text.toString().toDoubleOrNull(),
            minSurface = minSurfaceEditText.text.toString().toDoubleOrNull(),
            maxSurface = maxSurfaceEditText.text.toString().toDoubleOrNull(),
            nearSchools = nearSchoolsCheckBox.isChecked,
            nearRestaurants = nearRestaurantsCheckBox.isChecked,
            nearShops = nearShopsCheckBox.isChecked,
            nearBuses = nearBusesCheckBox.isChecked,
            nearTram = nearTramwayCheckBox.isChecked,
            nearPark = nearParkCheckBox.isChecked,
            isSold = isSoldSwitch.isChecked
        )

        //param this for livedata with viewmodel to create SQL query
        estateViewModel.performSearch(criteria)
        dismiss()
    }



}