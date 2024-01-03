package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.adapters.CreditSimulatorPagerAdapter


class LoanHostFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loan_host, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val viewPager: ViewPager2 = view.findViewById(R.id.loan_host_fragment_viewpager)
        val tabLayout: TabLayout = view.findViewById(R.id.loan_host_fragment_tablayout)

        val pagerAdapter = CreditSimulatorPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter

        // Use TabLayoutMediator to connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Loan Simulation"
                1 -> "Repayment Plan"
                else -> null
            }
        }.attach()

        return view
    }
}