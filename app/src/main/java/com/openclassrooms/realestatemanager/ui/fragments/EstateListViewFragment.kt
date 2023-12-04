package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.repositories.EstateItemClickListener
import com.openclassrooms.realestatemanager.ui.adapters.EstateListAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstateListViewFragment : Fragment(), EstateItemClickListener {

    private lateinit var estateListRecyclerView: RecyclerView
    private lateinit var noPropertyTextView: TextView
    private lateinit var estateListAdapter: EstateListAdapter
    private lateinit var propertyList: List<Property>

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_estate_list_view, container, false)

        setHasOptionsMenu(true)
        //(requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        // Initialization recyclerview and adapter
        estateListRecyclerView = view.findViewById(R.id.estate_list_recyclerview)
        noPropertyTextView = view.findViewById(R.id.no_property_textview)

        // Define Layout
        estateListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize an empty property list
        propertyList = emptyList()
        // Initialize adapter with an empty list
        estateListAdapter = EstateListAdapter(propertyList)
        // Attach adapter
        estateListRecyclerView.adapter = estateListAdapter

        // Observe livedata
        estateViewModel.getPropertyList().observe(viewLifecycleOwner) { propertyListLiveData ->
            Log.d("TESTDATA", "$propertyListLiveData")
            if (propertyListLiveData.isEmpty()) {
                // ANY PROPERTY ?
                setViewVisibility(estateListRecyclerView, View.GONE)
                setViewVisibility(noPropertyTextView, View.VISIBLE)
            } else {
                // Properties ok in the list
                setViewVisibility(estateListRecyclerView, View.VISIBLE)
                setViewVisibility(noPropertyTextView, View.GONE)

                // update adapter with new list
                propertyList = propertyListLiveData
                estateListAdapter.updateData(propertyListLiveData)

            }
        }

        // Ajoute un OnClickListener pour les éléments de la RecyclerView
        estateListAdapter.setOnItemClickListener { selectedItem ->
            estateViewModel.setSelectedProperty(selectedItem)
            // Navigue vers le fragment détaillé en utilisant le NavController
            findNavController().navigate(R.id.action_estateListViewFragment_to_estateDetailViewFragment)
        }

        return view
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_list, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setViewVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    override fun onEstateItemClick(property: Property) {
        // define selected property in the viewmodel
        estateViewModel.setSelectedProperty(property)
        Log.d("TESTDATA", " estate list fragment  / voici la propriété : $property")

        // Open the fragment "detail" with data of estate selected
        val detailFragment = EstateDetailViewFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, detailFragment)
            .commit()
    }
    override fun onResume() {
        super.onResume()
        Log.d("ETAT", "list fragment onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("ETAT", "list fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ETAT", "list fragment onStop")
    }


}