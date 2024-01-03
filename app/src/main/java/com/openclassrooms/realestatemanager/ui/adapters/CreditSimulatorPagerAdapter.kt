package com.openclassrooms.realestatemanager.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.openclassrooms.realestatemanager.ui.fragments.LoanMaxSimulatorFragment
import com.openclassrooms.realestatemanager.ui.fragments.LoanMonthlySimulatorFragment

class CreditSimulatorPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2 // Nombre total d'onglets
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoanMaxSimulatorFragment()
            1 -> LoanMonthlySimulatorFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}