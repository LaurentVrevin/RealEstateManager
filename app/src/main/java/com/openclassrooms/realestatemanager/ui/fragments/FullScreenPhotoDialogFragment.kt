package com.openclassrooms.realestatemanager.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.ui.adapters.DetailPhotoPagerAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel

class FullScreenPhotoDialogFragment(private val photos: List<Photo>, private val currentPosition: Int) : DialogFragment() {

    private lateinit var photoList: List<Photo>
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: DetailPhotoPagerAdapter

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_photo_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.fullscreen_photo_dialog_viewpager)
        adapter = DetailPhotoPagerAdapter(photos)

            viewPager.adapter = adapter
            viewPager.currentItem = currentPosition

        estateViewModel.selectedProperty.observe(viewLifecycleOwner) { property ->
            // Update photolist when property selected changed, update adapter with new list
            photoList = property.photos
            adapter.updateData(photoList)

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}