package com.openclassrooms.realestatemanager.ui.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore

import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.helper.PropertyTypeAdapterHelper
import com.openclassrooms.realestatemanager.ui.adapters.AddEstatePhotoAdapter
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class AddEstateActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 100
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_CAMERA_PERMISSION = 101
        private val REQUEST_IMAGE_CAPTURE = 2
    }
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estate_add)

        Log.d("addEstateCycle", "AddEstateActivity - onCreate")

        val takeFromGalleryButton: Button = findViewById(R.id.take_from_gallery_button)
        val takeFromCameraButton: Button = findViewById(R.id.take_photography_button)

        takeFromGalleryButton.setOnClickListener {
            openGallery()
        }
        takeFromCameraButton.setOnClickListener{
            openCamera()
        }

        setupActionBar()
        initViews()
        setupAutoComplete()
        initRecyclerView()
        initMap()
        PropertyTypeAdapterHelper.createAdapter(this, typeOfPropertyEditText)
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
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPhotos_add_estate_activity)
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

        if (mapContainer.visibility == View.VISIBLE) {
            // Update address when address selected
            updateMapWithLocation()
        }
    }

    private fun updateMapWithLocation() {
        val propertyLocation = LatLng(selectedLatitude, selectedLongitude)
        googleMap.clear()
        if (propertyLocation != null) {
            googleMap.addMarker(MarkerOptions().position(propertyLocation).title("Property Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLocation, 10f))
        }
    }


    private fun updateAddressFields(place: Place) {
        selectedLatitude = place.latLng.latitude
        selectedLongitude = place.latLng.longitude
        updateMapWithLocation()

        val geocoder = Geocoder(this)
        try {
            val addresses: List<Address> = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 3) as List<Address>

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
    //-- ADD PICTURE FROM CAMERA ---//

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, ask to the user
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            // Permission already granted, onpen camera
            launchCamera()
        }
    }
    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun saveCapturedPhoto(imageBitmap: Bitmap): Uri? {
        // give a filename to the image with pattern
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"

        // Make a content
        val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        // Use ContentResolver to insert the image in the store
        val contentResolver = applicationContext.contentResolver
        val imageUri = contentResolver.insert(imageCollection, imageDetails)

        try {
            // Open the exit flux to write the image in Uri
            val outputStream = contentResolver.openOutputStream(imageUri!!)
            if (outputStream != null) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            outputStream?.close()

            // The picture is record with succeed in the store
            // Uri fo picture can use to refefrence the recorded image

            return imageUri
        } catch (e: IOException) {
            e.printStackTrace()
            // Manage error
            return null
        }
    }

    //--- ADD PICTURE FROM GALLERY ---//

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For  >= Android 10
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        } else {
            // For version <Android 10, check the permission : READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGalleryForOlderVersions()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
            }
        }
    }

    private fun openGalleryForOlderVersions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGalleryForOlderVersions()
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        }
    }

    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.also { uri ->
                        //Flags are used to ask read and writing access to the file
                        val takeFlags: Int = data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                        // Check and take persitent permissions for Uri
                        contentResolver.takePersistableUriPermission(uri, takeFlags)

                        // use : uri
                        showAddPhotoDialog(uri)
                    }
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Manage image capture from camera
                    val imageBitmap = data?.extras?.get("data") as Bitmap


                    // After the photo was captured and get the bitmap, show the dialog to rename picture
                    val imageUri = saveCapturedPhoto(imageBitmap)
                    showAddPhotoDialog(imageUri)
                }
            }
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun showAddPhotoDialog(imageUri: Uri?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_photo, null)
        val editTextPhotoName: EditText = dialogView.findViewById(R.id.dialog_add_photo_editTextPhotoNameEdit)

        MaterialAlertDialogBuilder(this, R.style.MyAlertDialogStyle)
            .setTitle(R.string.add_estate_activity_dialog_add_title)
            .setView(dialogView)
            .setPositiveButton(R.string.add_estate_activity_dialog_add_add_button) { dialog, _ ->
                val photoName = editTextPhotoName.text.toString()
                if (imageUri != null && photoName.isNotEmpty()) {
                    val photo = Photo(UUID.randomUUID().toString(), imageUri.toString(), photoName)
                    photoList.add(photo)
                    addEstatePhotoAdapter.notifyDataSetChanged()
                } else {
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
        )
    }

    private fun saveEstate() {
        if (checkIfAllFieldsFilled()) {
            val newProperty = createPropertyFromInput()
            propertyDataList.add(newProperty)
            estateViewModel.addProperty(newProperty)
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
        val dateAdded = Utils.formattedTodayDate

        //if property is not sold, dateSold & agentId can be null for the moment
        var dateSold: Date? = null
        var agentId: String = ""
        var isSold = false

        // If sold, update these :
       /* if (/* property is sold so */) {
            dateSold = /* get the date of sell */
                agentId = /* id of agent */
                isSold = true
        }*/
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
            isParkNearby,
            selectedLatitude,
            selectedLongitude,
            dateAdded,
            dateSold,
            agentId,
            isSold
        )
    }

    private fun showDialog(titleResId: Int, messageResId: Int) {

            MaterialAlertDialogBuilder(this, R.style.MyAlertDialogStyle)
                .setTitle(getString(titleResId))
                .setMessage(getString(messageResId))
                .setPositiveButton(R.string.add_estate_activity_dialog_add_ok_button, null)
                .show()
    }
    override fun onResume() {
        super.onResume()
        if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE)) {
            // La permission est accordée, vous pouvez procéder à l'ouverture de la galerie
        }
    }
}


