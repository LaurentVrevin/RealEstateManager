package com.openclassrooms.realestatemanager.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.checkbox.MaterialCheckBox

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.ui.adapters.AddEstatePhotoAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import java.util.UUID

@AndroidEntryPoint
class AddEstateActivity : AppCompatActivity(), OnMapReadyCallback {


    private val PICK_IMAGE_REQUEST = 1
    private lateinit var photoList: ArrayList<Photo>
    private val propertyDataList = mutableListOf<Property>()
    private lateinit var addEstatePhotoAdapter: AddEstatePhotoAdapter
    private lateinit var descriptionEditText: EditText
    private lateinit var typeOfPropertyEditText: AutoCompleteTextView
    private lateinit var priceOfPropertyEditText: EditText
    private lateinit var surfaceOfPropertyEditText: EditText
    private lateinit var numberOfRoomsEditText: EditText
    private lateinit var numberOfBedroomsEditText: EditText
    private lateinit var numberOfBathroomsEditText: EditText
    private lateinit var addressOfPropertyTxt: TextView
    private lateinit var cityOfPropertyTxt: TextView
    private lateinit var countryOfPropertyTxt: TextView
    private lateinit var mapContainer: FrameLayout

    private lateinit var checkboxSchools: MaterialCheckBox
    private lateinit var checkboxRestaurants: MaterialCheckBox
    private lateinit var checkboxShops: MaterialCheckBox
    private lateinit var checkboxBuses: MaterialCheckBox
    private lateinit var checkboxTramway: MaterialCheckBox
    private lateinit var checkboxPark: MaterialCheckBox

    private val estateViewModel: EstateViewModel by viewModels()
    private val placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    private lateinit var googleMap: GoogleMap
    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estate_add)

        val takeFromGalleryButton: Button = findViewById(R.id.take_from_gallery_button)

        takeFromGalleryButton.setOnClickListener {
            openGallery()
        }
        setupActionBar()
        initViews()
        setupAutoComplete()
        initRecyclerView()
        initMap()

        val propertyTypesList = ArrayList<String>()
        propertyTypesList.add("Villa")
        propertyTypesList.add("Manoir")
        propertyTypesList.add("Appartement")
        propertyTypesList.add("Maison")
        propertyTypesList.add("Loft")

        val propertyTypeAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, propertyTypesList)
        typeOfPropertyEditText.setAdapter(propertyTypeAdapter)
    }
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        descriptionEditText = findViewById(R.id.til_description_edit)
        typeOfPropertyEditText = findViewById(R.id.til_type_of_property_autocomplete)
        priceOfPropertyEditText = findViewById(R.id.til_price_of_property_edit)
        surfaceOfPropertyEditText = findViewById(R.id.til_surface_of_property_edit)
        numberOfRoomsEditText = findViewById(R.id.til_number_of_rooms_edit)
        numberOfBedroomsEditText = findViewById(R.id.til_number_of_bedrooms_edit)
        numberOfBathroomsEditText = findViewById(R.id.til_number_of_bathrooms_edit)
        addressOfPropertyTxt = findViewById(R.id.txt_address_of_property)
        cityOfPropertyTxt = findViewById(R.id.txt_city_of_property)
        countryOfPropertyTxt = findViewById(R.id.txt_country_of_property)
        mapContainer = findViewById(R.id.add_estate_mapContainer)

        //CHECKBOX
        checkboxSchools = findViewById(R.id.checkbox_schools)
        checkboxRestaurants = findViewById(R.id.checkbox_restaurants)
        checkboxShops = findViewById(R.id.checkbox_Shops)
        checkboxBuses = findViewById(R.id.checkbox_buses)
        checkboxTramway = findViewById(R.id.checkbox_tramway)
        checkboxPark = findViewById(R.id.checkbox_park)

        //VISIBILITY OF ELEMENTS
        addressOfPropertyTxt.visibility = View.GONE
        cityOfPropertyTxt.visibility = View.GONE
        countryOfPropertyTxt.visibility = View.GONE
        mapContainer.visibility = View.GONE
    }

    private fun setupAutoComplete() {
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteFragment?.setPlaceFields(placeFields)
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                updateAddressFields(place)
                selectedLatitude = place.latLng?.latitude ?: 0.0
                selectedLongitude = place.latLng?.longitude ?: 0.0
                showAddressFields()
            }

            override fun onError(status: Status) {
                Log.e("TAG", "Error: ${status.statusMessage}")
            }
        })
    }

    private fun showAddressFields() {
        mapContainer.visibility = View.VISIBLE
        addressOfPropertyTxt.visibility = View.VISIBLE
        cityOfPropertyTxt.visibility = View.VISIBLE
        countryOfPropertyTxt.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        photoList = ArrayList()
        addEstatePhotoAdapter = AddEstatePhotoAdapter(photoList)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPhotos)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = addEstatePhotoAdapter
    }
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.add_estate_mapContainer) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            onMapReady(googleMap)
        }
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        // La carte est initialement cachée, alors ne la mettez à jour que si une adresse est sélectionnée
        if (mapContainer.visibility == View.VISIBLE) {
            // Mettez à jour la carte lorsque l'adresse est sélectionnée
            updateMapWithLocation()
        }
    }

    private fun updateMapWithLocation() {
        val propertyLocation = LatLng(selectedLatitude, selectedLongitude)
        googleMap.clear()
        if (propertyLocation != null) {
            googleMap.addMarker(MarkerOptions().position(propertyLocation).title("Property Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLocation, 15f))
        }
    }

    private fun updateAddressFields(place: Place) {
        selectedLatitude = place.latLng?.latitude ?: 0.0
        selectedLongitude = place.latLng?.longitude ?: 0.0
        updateMapWithLocation()

        val geocoder = Geocoder(this)
        try {
            val addresses: List<Address> = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 1) as List<Address>

            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val city = address.locality
                val street = address.thoroughfare
                val fullStreet = place.name
                val country = address.countryName

                addressOfPropertyTxt.text = fullStreet
                cityOfPropertyTxt.text = city
                countryOfPropertyTxt.text = country

                Log.d("ADDRESS", "City: $city, Street: $street, Country: $country, full adress : $fullStreet")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ADDRESS", "Error retrieving address details: ${e.message}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_estate, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_estate_toolbar_action_save -> {
                saveEstate()
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //--- ADD PICTURE FROM GALLERY ---//
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Retrieve the image URI
            val selectedImage = data.data

            // Show the dialog to ask for the name
            showAddPhotoDialog(selectedImage)
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showAddPhotoDialog(imageUri: Uri?) {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_photo, null)
        val editTextPhotoName: EditText =
            dialogView.findViewById(R.id.dialog_add_photo_editTextPhotoNameEdit)

        MaterialAlertDialogBuilder(this, R.style.MyAlertDialogStyle)
            .setTitle(R.string.add_estate_activity_dialog_add_title)
            .setView(dialogView)
            .setPositiveButton(R.string.add_estate_activity_dialog_add_add_button) { dialog, _ ->
                val id = UUID.randomUUID().toString()
                val photoName = editTextPhotoName.text.toString()
                if (imageUri != null && photoName.isNotEmpty()) {
                    // Add the photo with the name to the lis
                    val photo = Photo(id, imageUri, photoName)
                    photoList.add(photo)
                    addEstatePhotoAdapter.notifyDataSetChanged()

                } else {
                    // Show an error message if the name is empty
                    showDialog(
                        R.string.add_estate_activity_dialog_add_error_title,
                        R.string.add_estate_activity_dialog_add_error_message
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.add_estate_activity_dialog_add_cancel_button) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    private fun checkIfFieldsFilled(vararg fields: EditText): Boolean {
        // Check if all fields are filled
        for (field in fields) {
            if (field.text.toString().isEmpty()) {
                showDialog(
                    R.string.add_estate_activity_dialog_save_error_title,
                    R.string.add_estate_activity_dialog_save_error_message
                )
                return false
            }
        }
        return true
    }

    private fun checkIfAllFieldsFilled(): Boolean {
        // Call previous method
        return checkIfFieldsFilled(
            descriptionEditText,
            typeOfPropertyEditText,
            priceOfPropertyEditText,
            surfaceOfPropertyEditText,
            numberOfRoomsEditText,
            numberOfBedroomsEditText,
            numberOfBathroomsEditText,
            /*addressOfPropertyTxt,
            cityOfPropertyTxt,
            countryOfPropertyTxt*/
        )
    }

    private fun saveEstate() {
        if (checkIfAllFieldsFilled()) {
            val newProperty = createPropertyFromInput()
            propertyDataList.add(newProperty)
            estateViewModel.addProperty(newProperty)
            showDialog(R.string.add_estate_activity_dialog_add_success_title, R.string.add_estate_activity_dialog_add_success_message)
            finish()
        }
    }
    private fun createPropertyFromInput(): Property {

        val isSchoolsNearby = checkboxSchools.isChecked
        val isRestaurantsNearby = checkboxRestaurants.isChecked
        val isShopsNearby = checkboxShops.isChecked
        val isBusesNearby = checkboxBuses.isChecked
        val isTramwayNearby = checkboxTramway.isChecked
        val isParkNearby = checkboxPark.isChecked
        return Property(
            UUID.randomUUID().toString(),
            descriptionEditText.text.toString(),
            typeOfPropertyEditText.text.toString(),
            priceOfPropertyEditText.text.toString(),
            surfaceOfPropertyEditText.text.toString(),
            numberOfRoomsEditText.text.toString(),
            numberOfBedroomsEditText.text.toString(),
            numberOfBathroomsEditText.text.toString(),
            addressOfPropertyTxt.text.toString(),
            cityOfPropertyTxt.text.toString(),
            countryOfPropertyTxt.text.toString(),
            photoList,
            isSchoolsNearby,
            isRestaurantsNearby,
            isShopsNearby,
            isBusesNearby,
            isTramwayNearby,
            isParkNearby
        )
    }


    private fun showDialog(titleResId: Int, messageResId: Int) {
        MaterialAlertDialogBuilder(this, R.style.MyAlertDialogStyle)
            .setTitle(getString(titleResId))
            .setMessage(getString(messageResId))
            .setPositiveButton(R.string.add_estate_activity_dialog_add_ok_button, null)
            .show()
    }

}


