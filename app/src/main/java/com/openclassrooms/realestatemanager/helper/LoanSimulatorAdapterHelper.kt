package com.openclassrooms.realestatemanager.helper

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class LoanSimulatorAdapterHelper {
    companion object {
        fun createAdapter(context: Context, autoCompleteTextView: AutoCompleteTextView) {
            val loanTermsList = ArrayList<Int>()
            loanTermsList.add(10)
            loanTermsList.add(15)
            loanTermsList.add(20)
            loanTermsList.add(25)

            val propertyTypeAdapter =
                ArrayAdapter(context, R.layout.simple_dropdown_item_1line, loanTermsList)
            autoCompleteTextView.setAdapter(propertyTypeAdapter)
        }
    }
}