package com.openclassrooms.realestatemanager.helper

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class AgentAdapterHelper {

    companion object {
        fun createAdapter(context: Context, autoCompleteTextView: AutoCompleteTextView) {
            val agentList = ArrayList<String>()
            agentList.add("Kurosaki I.")
            agentList.add("Zaraki K.")

            val propertyTypeAdapter =
                ArrayAdapter(context, R.layout.simple_dropdown_item_1line, agentList)
            autoCompleteTextView.setAdapter(propertyTypeAdapter)
        }
    }
}