package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.helper.PropertyTypeAdapterHelper


class PropertySearchDialogFragment : DialogFragment() {

    private lateinit var searchButton: Button
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_property_search_dialog, container, false)

        searchButton = view.findViewById(R.id.fragment_property_search_dialog_search_button)
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PropertyTypeAdapterHelper.createAdapter(requireContext(), typeOfPropertyEditText)
    }


}