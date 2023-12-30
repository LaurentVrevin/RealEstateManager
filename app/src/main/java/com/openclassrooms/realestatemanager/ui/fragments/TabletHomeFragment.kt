package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel


class TabletHomeFragment : Fragment() {

    private lateinit var textviewNoProperty:TextView

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tablet_home_fragment, container, false)

        textviewNoProperty = view.findViewById(R.id.tablet_home_fragment_noproperty_textview)
        textviewNoProperty.visibility=GONE

        // load fragments of list and detail
        val listFragment = EstateListViewFragment()
        val detailFragment = EstateDetailViewFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.listview_fragment_container, listFragment)
            .replace(R.id.detail_fragment_container, detailFragment)
            .commit()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe list of properties, and select the first for detail fragment
        estateViewModel.propertyList.observe(viewLifecycleOwner) { properties ->
            if (properties.isNotEmpty()) {
                textviewNoProperty.visibility=GONE
                estateViewModel.setSelectedPropertyId(properties.first().id)

            }
            else{
                textviewNoProperty.visibility= VISIBLE


            }
        }
    }


}