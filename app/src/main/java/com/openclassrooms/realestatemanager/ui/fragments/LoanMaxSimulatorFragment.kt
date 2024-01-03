package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.helper.LoanSimulatorAdapterHelper

class LoanMaxSimulatorFragment : Fragment() {

    private lateinit var calculateButton: Button
    private lateinit var incomeEditText: TextInputEditText
    private lateinit var repaymentEditText: TextInputEditText
    private lateinit var numberYearsSelected: AutoCompleteTextView
    private lateinit var interestRateEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var resultWithInterestTextView: TextView
    private lateinit var loanSimulationCardView: CardView


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
        numberYearsSelected = view.findViewById(R.id.nbr_years_selected_autocomplete)
        interestRateEditText = view.findViewById(R.id.interestRateEditText)
        resultTextView = view.findViewById(R.id.result_textview)
        resultWithInterestTextView = view.findViewById(R.id.loan_cost_textview)
        loanSimulationCardView = view.findViewById(R.id.loan_simulation_cardview_result_textview)

        loanSimulationCardView.visibility=View.GONE

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

        val incomeStr = incomeEditText.text.toString()
        val repaymentStr = repaymentEditText.text.toString()
        val numberYearsStr = numberYearsSelected.text.toString()
        // check is any edittext is empty
        if (incomeStr.isBlank() || repaymentStr.isBlank() || numberYearsStr.isBlank()) {
            // show message error !
            Toast.makeText(context, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
            loanSimulationCardView.visibility = View.GONE
            return
        }else {

        // Get values entered by the user
        val income = incomeEditText.text.toString().toDoubleOrNull() ?: 0.0
        val repayment = repaymentEditText.text.toString().toDoubleOrNull() ?: 0.0
        val interestRate = interestRateEditText.text.toString().toDoubleOrNull() ?: 0.0


        val minOfMonthlyRepayment = 0.30
        // Calculate the maximum total amount to borrow
        val maxPossibleMonthlyRepayment = income * minOfMonthlyRepayment

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

            // Calculate the total amount to pay and set the result text using a formatted string
            val amountBorrowed = selectedNumberOfYears * maxYearlyRepayment
            val totalToPay =
                selectedNumberOfYears * maxYearlyRepayment * (1 + interestRate / 100)

            // Format the result to display only two decimal places
            val formattedAmountBorrowed = String.format("%.2f", amountBorrowed)
            val formattedTotalToPay = String.format("%.2f", totalToPay)
            resultTextView.text =
                getString(R.string.fragment_loan_simulation_result_label) + formattedAmountBorrowed + getString(
                    R.string.currency_euros
                )
            resultWithInterestTextView.text =
                getString(R.string.fragment_loan_simulation_result_with_interest_label) + formattedTotalToPay + getString(
                    R.string.currency_euros
                )
            loanSimulationCardView.visibility = View.VISIBLE
        }
        }
    }

}