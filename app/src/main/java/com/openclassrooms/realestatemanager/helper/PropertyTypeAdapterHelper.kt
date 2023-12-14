package com.openclassrooms.realestatemanager.helper

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.openclassrooms.realestatemanager.R

class PropertyTypeAdapterHelper {

    companion object {
        fun createAdapter(context: Context, autoCompleteTextView: AutoCompleteTextView) {
            val propertyTypesList = ArrayList<String>().apply {
                add(context.getString(R.string.property_type_adapter_helper_villa))
                add(context.getString(R.string.property_type_adapter_helper_estate))
                add(context.getString(R.string.property_type_adapter_helper_manor))
                add(context.getString(R.string.property_type_adapter_helper_apartment))
                add(context.getString(R.string.property_type_adapter_helper_loft))
                add(context.getString(R.string.property_type_adapter_helper_penthouse))
            }

            val propertyTypeAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, propertyTypesList)
            autoCompleteTextView.setAdapter(propertyTypeAdapter)
        }
    }
}