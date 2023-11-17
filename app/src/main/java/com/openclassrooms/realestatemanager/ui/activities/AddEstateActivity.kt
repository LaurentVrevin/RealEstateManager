package com.openclassrooms.realestatemanager.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Estate
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.ui.adapters.AddEstatePhotoAdapter
import java.util.UUID

class AddEstateActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var photoList: ArrayList<Photo>
    private lateinit var addEstatePhotoAdapter: AddEstatePhotoAdapter
    private lateinit var descriptionEditText: EditText
    private lateinit var typeOfPropertyEditText: AutoCompleteTextView
    private lateinit var priceOfPropertyEditText: EditText
    private lateinit var surfaceOfPropertyEditText: EditText
    private lateinit var numberOfRoomsEditText: EditText
    private lateinit var numberOfBedroomsEditText: EditText
    private lateinit var numberOfBathroomsEditText: EditText
    private lateinit var addressOfPropertyEditText: EditText
    private lateinit var postalCodeOfPropertyEditText: EditText
    private lateinit var countryOfPropertyEditText: EditText

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estate_add)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val takeFromGalleryButton: Button = findViewById(R.id.take_from_gallery_button)
        descriptionEditText = findViewById(R.id.til_description_edit)
        typeOfPropertyEditText = findViewById(R.id.til_type_of_property_autocomplete)
        priceOfPropertyEditText = findViewById(R.id.til_price_of_property_edit)
        surfaceOfPropertyEditText = findViewById(R.id.til_surface_of_property_edit)
        numberOfRoomsEditText = findViewById(R.id.til_number_of_rooms_edit)
        numberOfBedroomsEditText = findViewById(R.id.til_number_of_bedrooms_edit)
        numberOfBathroomsEditText = findViewById(R.id.til_number_of_bathrooms_edit)
        addressOfPropertyEditText = findViewById(R.id.til_address_of_property_edit)
        postalCodeOfPropertyEditText = findViewById(R.id.til_postal_code_of_property_edit)
        countryOfPropertyEditText = findViewById(R.id.til_country_of_property_edit)

        // Initialize the list and the adapter
        photoList = ArrayList()
        addEstatePhotoAdapter = AddEstatePhotoAdapter(photoList)

        // Configure the RecyclerView with a GridLayoutManager
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPhotos)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = addEstatePhotoAdapter

        takeFromGalleryButton.setOnClickListener {
            openGallery()
        }
        val propertyTypesList = ArrayList<String>()
        propertyTypesList.add("Villa")
        propertyTypesList.add("Manoir")
        propertyTypesList.add("Appartement")
        propertyTypesList.add("Maison")
        propertyTypesList.add("Loft")

        val propertyTypeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, propertyTypesList)
        typeOfPropertyEditText.setAdapter(propertyTypeAdapter)
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
        val editTextPhotoName: EditText = dialogView.findViewById(R.id.dialog_add_photo_editTextPhotoNameEdit)

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
                    showDialog(R.string.add_estate_activity_dialog_add_error_title, R.string.add_estate_activity_dialog_add_error_message)
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
                showDialog(R.string.add_estate_activity_dialog_save_error_title, R.string.add_estate_activity_dialog_save_error_message)
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
            addressOfPropertyEditText,
            postalCodeOfPropertyEditText,
            countryOfPropertyEditText
        )
    }
    private fun saveEstate() {
        // TODO : Ajouter la logique pour sauvegarder le bien immobilier
        if (checkIfAllFieldsFilled()) {
            // Créer un nouvel objet Estate
            val newEstate = Estate(
                descriptionEditText.text.toString(),
                typeOfPropertyEditText.text.toString(),
                priceOfPropertyEditText.text.toString(),
                surfaceOfPropertyEditText.text.toString(),
                numberOfRoomsEditText.text.toString(),
                numberOfBedroomsEditText.text.toString(),
                numberOfBathroomsEditText.text.toString(),
                addressOfPropertyEditText.text.toString(),
                postalCodeOfPropertyEditText.text.toString(),
                countryOfPropertyEditText.text.toString(),
                photoList
            )

            // Ajouter le nouvel objet Estate à une liste (vous devrez gérer cette liste dans votre architecture)
            // estateList.add(newEstate)

            // TODO : Ajouter la logique pour sauvegarder la liste de biens immobiliers

            // Afficher le message de succès
            showDialog(R.string.add_estate_activity_dialog_add_success_title, R.string.add_estate_activity_dialog_add_success_message)
        }
    }

    private fun showDialog(titleResId: Int, messageResId: Int) {
        MaterialAlertDialogBuilder(this, R.style.MyAlertDialogStyle)
            .setTitle(getString(titleResId))
            .setMessage(getString(messageResId))
            .setPositiveButton(R.string.add_estate_activity_dialog_add_ok_button, null)
            .show()
    }


}