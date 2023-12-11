package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.helper.LoanSimulatorAdapterHelper

class LoanSimulationFragment : Fragment() {

    private lateinit var calculateButton: Button
    private lateinit var incomeEditText: TextInputEditText
    private lateinit var repaymentEditText: TextInputEditText
    private lateinit var depositEditText: TextInputEditText
    private lateinit var numberYearsSelected: AutoCompleteTextView
    private lateinit var interestRateTextView: TextView
    private lateinit var resultTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loan_simulation, container, false)

        // Initialize views and button
        calculateButton = view.findViewById(R.id.calculateButton)
        incomeEditText = view.findViewById(R.id.incomeEditText)
        repaymentEditText = view.findViewById(R.id.repaymentEditText)
        depositEditText = view.findViewById(R.id.depositEditText)
        numberYearsSelected = view.findViewById(R.id.nbr_years_selected_autocomplete)
        interestRateTextView = view.findViewById(R.id.interestRateLayout)
        resultTextView = view.findViewById(R.id.resultTextView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize adapter for autocomplete
        LoanSimulatorAdapterHelper.createAdapter(requireContext(), numberYearsSelected)

        // Attach event handler to the calculate button
        calculateButton.setOnClickListener {
            calculateLoan()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateLoan() {
        // Get values entered by the user
        val income = incomeEditText.text.toString().toDoubleOrNull() ?: 0.0
        val repayment = repaymentEditText.text.toString().toDoubleOrNull() ?: 0.0
        val deposit = depositEditText.text.toString().toDoubleOrNull() ?: 0.0
        val interestRates = listOf(
            Pair(10, 2),
            Pair(15, 2.5),
            Pair(20, 3),
            Pair(25, 3.5)
        )

        // Calculate the maximum total amount to borrow
        val maxPossibleMonthlyRepayment = income * 0.30

        // Check if the desired monthly amount exceeds 30% of the monthly income
        if (repayment > maxPossibleMonthlyRepayment) {

            // Display an error message with Toast
            Toast.makeText(requireContext(),
                R.string.fragment_loan_simulation_monthly_repayment_error,
                Toast.LENGTH_SHORT).show()

            // You can also highlight the input field or perform other actions on error
        } else {
            // If it's okay, proceed with the calculation
            // The annual amount is equal to the monthly amount multiplied by 12
            val maxYearlyRepayment = repayment * 12
            // Get the number of years chosen by the user
            val numberOfYearsSelected = numberYearsSelected.text.toString().toDouble()
            val selectedNumberOfYears = numberOfYearsSelected.toInt()
            // Pass the percentage based on the number of years chosen by pairing in the list
            val interestRate = interestRates.find { it.first == selectedNumberOfYears }?.second ?: 0.0

            // Set the interest rate text using a formatted string
            interestRateTextView.text = getString(R.string.fragment_loan_simulation_interest_rate_label) + "$interestRate%"

            // Calculate the total amount to pay and set the result text using a formatted string
            val totalToPay = selectedNumberOfYears * maxYearlyRepayment * (1 + interestRate.toDouble() / 100) + deposit
            resultTextView.text = getString(R.string.fragment_loan_simulation_result_label) + totalToPay.toString() + getString(R.string.currency_euros)


        }
    }

}