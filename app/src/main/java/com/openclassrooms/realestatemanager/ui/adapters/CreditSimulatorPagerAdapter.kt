package com.openclassrooms.realestatemanager.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.openclassrooms.realestatemanager.ui.fragments.LoanSimulationFragment
import com.openclassrooms.realestatemanager.ui.fragments.RepaymentPlanFragment

class CreditSimulatorPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2 // Nombre total d'onglets
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoanSimulationFragment()
            1 -> RepaymentPlanFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}