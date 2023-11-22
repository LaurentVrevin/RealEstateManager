package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.adapters.EstateListAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstateListViewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noPropertyTextView: TextView
    private lateinit var estateListAdapter: EstateListAdapter

    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_estate_list_view, container, false)

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_list, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialization recyclerview and adapter
        recyclerView = view.findViewById(R.id.estate_list_recyclerview)
        noPropertyTextView = view.findViewById(R.id.no_property_textview)
        estateListAdapter = EstateListAdapter()

        // Define Layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Attach adapter
        recyclerView.adapter = estateListAdapter

        // Observe livedata
        estateViewModel.getPropertyList().observe(viewLifecycleOwner) { propertyList ->
            if (propertyList.isEmpty()) {
                // ANY PROPERTY ?
                setViewVisibility(recyclerView, View.GONE)
                setViewVisibility(noPropertyTextView, View.VISIBLE)
            } else {
                // Properties ok in the list
                setViewVisibility(recyclerView, View.VISIBLE)
                setViewVisibility(noPropertyTextView, View.GONE)

                // update adapter with new list
                estateListAdapter.updateData(propertyList)
            }
        }

    }
    private fun setViewVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }


}