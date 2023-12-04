package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.activities.MainActivity
import com.openclassrooms.realestatemanager.ui.adapters.EstateDetailPhotoAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstateDetailViewFragment : Fragment() {

    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoAdapter: EstateDetailPhotoAdapter
    private var isFavorite = false


    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_estate_detail_view, container, false)

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize RecyclerView and Adapter
        photoRecyclerView = view.findViewById(R.id.fragment_estate_detail_view_photoRecyclerView)
        photoAdapter = EstateDetailPhotoAdapter() // Pass an empty list for now
        val txtview_detailfragment = view.findViewById<TextView>(R.id.txtview_detailfragment)

        // Set up RecyclerView
        photoRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        photoRecyclerView.adapter = photoAdapter

        estateViewModel.getSelectedProperty().observe(viewLifecycleOwner){ property ->
            if (property.city ==""){
                txtview_detailfragment.setText("pas de ville")
            }
            else
                txtview_detailfragment.setText(property.city)
            Log.d("TESTDATA", "voici la propriété : $property")
        }

        estateViewModel.getPropertyList().observe(viewLifecycleOwner) { propertyList ->

            Log.d("TESTDATA", "$propertyList")
        }


        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_detail, menu)
    }

    private fun toggleFavoriteIcon(item: MenuItem) {
        //If is favorite so delete favorite, or do favorite
        if (isFavorite) {

            item.setIcon(R.drawable.baseline_favorite_border_24)
            isFavorite = false
        } else {

            item.setIcon(R.drawable.baseline_favorite_24)
            isFavorite = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.FavoriteIcon -> {
                // setup the click for favorite
                toggleFavoriteIcon(item)
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ETAT", "detail fragment onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("ETAT", "detail fragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ETAT", "detail fragment onStop")
    }


}