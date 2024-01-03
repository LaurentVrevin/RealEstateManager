package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.ui.adapters.EstateListAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListViewFragment : Fragment(){

    private lateinit var estateListRecyclerView: RecyclerView
    private lateinit var noPropertyTextView: TextView
    private lateinit var estateListAdapter: EstateListAdapter
    private lateinit var propertyList: List<Property>
    private lateinit var searchFab:FloatingActionButton
    private var callback: OnSearchButtonClickListener? = null
    private var isCurrencyEuro:Boolean=false


    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_estate_list_view, container, false)
        setHasOptionsMenu(true)

        val isTablet: Boolean by lazy {
            resources.getBoolean(R.bool.isTablet)
        }
        //(requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        // Initialization recyclerview and adapter
        estateListRecyclerView = view.findViewById(R.id.estate_list_fragment_recyclerview)
        noPropertyTextView = view.findViewById(R.id.estate_list_fragment_noproperty_textview)
        searchFab = view.findViewById(R.id.search_property_fab)
        searchFab.setOnClickListener { callback?.onSearchButtonClick() }

        // Define Layout
        estateListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize an empty property list
        propertyList = emptyList()
        // Initialize adapter with an empty list
        estateListAdapter = EstateListAdapter(propertyList, isCurrencyEuro)
        // Attach adapter
        estateListRecyclerView.adapter = estateListAdapter

        // Observe livedata
        estateViewModel.propertyList.observe(viewLifecycleOwner) { propertyListLiveData ->

            if (propertyListLiveData.isEmpty()) {
                // ANY PROPERTY ?
                setViewVisibility(estateListRecyclerView, View.GONE)
                setViewVisibility(noPropertyTextView, View.VISIBLE)
            } else {
                // Properties ok in the list
                setViewVisibility(estateListRecyclerView, View.VISIBLE)
                setViewVisibility(noPropertyTextView, View.GONE)
                estateViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
                    val isCurrencyEuro = currency == EstateViewModel.Currency.EUR
                    propertyList = propertyListLiveData
                    estateListAdapter.updateData(propertyList, isCurrencyEuro)
                }

            }
        }



        estateListAdapter.setOnItemClickListener { selectedItem ->
            estateViewModel.setSelectedPropertyId(selectedItem.id)
            if (isTablet) {
                //nothing to do
            } else {
                // Use navigation controller with action
                findNavController().navigate(R.id.action_estateListViewFragment_to_estateDetailViewFragment)

            }
        }

        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchFab.setOnClickListener {
            val dialog = PropertySearchDialogFragment()
            dialog.show(parentFragmentManager, "PropertySearchDialogFragment")
        }

        // Observe result from research
        estateViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (!results.isNullOrEmpty()) {
                estateListAdapter.updateData(results, isCurrencyEuro)


            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_list, menu)
        updateCurrencyIconVisibility(menu)
        }

    private fun updateCurrencyIconVisibility(menu: Menu) {
        estateViewModel.propertyList.observe(viewLifecycleOwner) { properties ->
            val currencyMenuItem = menu.findItem(R.id.CurrencyIcon)
            currencyMenuItem.isVisible = properties.isNotEmpty()
        }
    }


    private fun setViewVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    interface OnSearchButtonClickListener {
        fun onSearchButtonClick()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? OnSearchButtonClickListener
            ?: throw ClassCastException("$context must implement OnSearchButtonClickListener")
    }

    override fun onResume() {
        super.onResume()

    }

}