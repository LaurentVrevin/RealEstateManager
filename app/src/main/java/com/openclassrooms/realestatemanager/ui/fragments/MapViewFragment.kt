package com.openclassrooms.realestatemanager.ui.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapViewFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_view, container, false)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the map fragment asynchronously
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_position) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)

        } else {
            // I'll see about error
        }

    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        myPosition()
        observeProperties()
    }
    private fun observeProperties() {
        estateViewModel.propertyList.observe(viewLifecycleOwner) { properties ->
            googleMap.clear()
            properties.forEach { property ->
                addPropertyMarkers(property)
            }
        }
    }

    private fun addPropertyMarkers(property: Property) {
        val location = LatLng(property.latitude, property.longitude)
        val markerOptions = MarkerOptions()
            .position(location)
            .title(property.title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        googleMap.addMarker(markerOptions)
    }


    private fun myPosition(){
        // Check if permission is granted
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Enable the location layer. Request the last known location of the device.
            googleMap.isMyLocationEnabled = true

            // Get the last known location and move the camera there
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)

                    // Add a marker at the user's location
                    googleMap.addMarker(MarkerOptions().position(latLng).title("My Location"))

                    // Move the camera to the user's location with a zoom level of 15
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
            }
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onResume() {
        super.onResume()
        // wait googlemap is initialized with coroutine
        lifecycleScope.launch {
            waitForMapInitialization()
            myPosition()
        }
    }
    private suspend fun waitForMapInitialization() {
        while (!::googleMap.isInitialized) {
            // wait a delay, and check one more time
            delay(100)
        }
    }
}