package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.helper.LoanSimulatorAdapterHelper

class RepaymentPlanFragment : Fragment() {

    private lateinit var propertyPriceLayout: TextInputLayout
    private lateinit var propertyPriceEditText: TextInputEditText
    private lateinit var loanTermLayout: TextInputLayout
    private lateinit var loanTermAutocomplete: AutoCompleteTextView
    private lateinit var depositEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var resultWithDepositTextView: TextView
    private lateinit var repaymentCardView: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_repayment_plan, container, false)

        // Initialize views
        propertyPriceLayout = view.findViewById(R.id.repayment_fragment_price_of_property)
        propertyPriceEditText = view.findViewById(R.id.repayment_fragment_price_of_property_edittext)
        loanTermLayout = view.findViewById(R.id.repayment_fragment_loan_term)
        loanTermAutocomplete = view.findViewById(R.id.repayment_fragment_loan_term_autocomplete)
        depositEditText = view.findViewById(R.id.repayment_fragment_deposit_edittext)
        calculateButton = view.findViewById(R.id.repayment_fragment_calculate_button)
        resultTextView = view.findViewById(R.id.repayment_fragment_result_textview)
        resultWithDepositTextView = view.findViewById(R.id.repayment_fragment_result_with_deposit_textview)
        repaymentCardView = view.findViewById(R.id.repayment_cardview_result_textview)
        repaymentCardView.visibility = View.GONE

        LoanSimulatorAdapterHelper.createAdapter(requireContext(), loanTermAutocomplete)

        // Set click listener for the calculate button
        calculateButton.setOnClickListener {
            calculateRepayment()
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun calculateRepayment() {

        val propertyPriceStr = propertyPriceEditText.text.toString()
        val loanTermStr = loanTermAutocomplete.text.toString()
        val depositStr = depositEditText.text.toString()


        if (propertyPriceStr.isBlank() || loanTermStr.isBlank() || depositStr.isBlank()){
        // show message error !
        Toast.makeText(context, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
        repaymentCardView.visibility = View.GONE
        return
    }else{
            // Get values entered by the user
            val propertyPrice = propertyPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val loanTerm = loanTermAutocomplete.text.toString().toDoubleOrNull() ?: 0.0
            val deposit = depositEditText.text.toString().toDoubleOrNull() ?: 0.0


        // Validate that property price and loan term are greater than zero
        if (propertyPrice <= 0 || loanTerm <= 0 || deposit <0) {
            // Display an error message
            resultTextView.text = getString(R.string.fragment_repayment_plan_invalid_input)
            repaymentCardView.visibility = View.GONE
        } else {
            // Perform the calculation
            val amountToBorrow = propertyPrice
            val amountToBorrowWithDeposit = propertyPrice - deposit

            val monthlyRepayment = (amountToBorrow / loanTerm) / 12
            val monthlyRepaymentWithDeposit = (amountToBorrowWithDeposit / loanTerm) / 12
            // Format the result to display only two decimal places
            val formattedRepayment = String.format("%.2f", monthlyRepayment)
            val formattedRepaymentWithDeposit = String.format("%.2f", monthlyRepaymentWithDeposit)

            // Display the result
            resultTextView.text =
                getString(R.string.fragment_repayment_plan_result_label) + formattedRepayment + getString(
                    R.string.currency_euros
                )
            resultWithDepositTextView.text =
                getString(R.string.fragment_repayment_plan_result_with_deposit_label) + formattedRepaymentWithDeposit + getString(
                    R.string.currency_euros
                )
            repaymentCardView.visibility = View.VISIBLE
        }
        }
    }
}