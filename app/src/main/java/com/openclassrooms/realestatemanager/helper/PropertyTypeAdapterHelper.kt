package com.openclassrooms.realestatemanager.helper

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class PropertyTypeAdapterHelper {

    companion object {
        fun createAdapter(context: Context, autoCompleteTextView: AutoCompleteTextView) {
            val propertyTypesList = ArrayList<String>()
            propertyTypesList.add("Villa")
            propertyTypesList.add("Manoir")
            propertyTypesList.add("Appartement")
            propertyTypesList.add("Maison")
            propertyTypesList.add("Loft")

            val propertyTypeAdapter =
                ArrayAdapter(context, R.layout.simple_dropdown_item_1line, propertyTypesList)
            autoCompleteTextView.setAdapter(propertyTypeAdapter)
        }
    }
}