package com.openclassrooms.realestatemanager.ui.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2


import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.ui.adapters.DetailPhotoPagerAdapter


import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstateDetailViewFragment : Fragment() {

    private var isFavorite = false
    private lateinit var viewPagerPhotos: ViewPager2
    private lateinit var photoAdapter: DetailPhotoPagerAdapter

    private lateinit var titleTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTitleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var informationTitleTextView: TextView
    private lateinit var surfaceTextView: TextView
    private lateinit var numberRoomsTextView: TextView
    private lateinit var numberBedroomsTextView: TextView
    private lateinit var numberBathroomsTextView: TextView
    private lateinit var interestTitleTextView: TextView
    private lateinit var schoolTextView: TextView
    private lateinit var foodTextView: TextView
    private lateinit var shopTextView: TextView
    private lateinit var busTextView: TextView
    private lateinit var tramTextView: TextView
    private lateinit var parkTextView: TextView
    private var photoList: List<Photo> = emptyList()

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 1001
    }

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estate_detail_view, container, false)

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        return view
    }

    @SuppressLint("SetTextI18n", "UseCompatTextViewDrawableApis")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewPager
        viewPagerPhotos = view.findViewById(R.id.estate_detail_fragment_viewpager_photos)
        titleTextView = view.findViewById(R.id.estate_detail_fragment_title_textview)
        cityTextView = view.findViewById(R.id.estate_detail_fragment_cityname_textview)
        priceTextView = view.findViewById(R.id.estate_detail_fragment_price_textview)
        descriptionTitleTextView =
            view.findViewById(R.id.estate_detail_fragment_description_title_textview)
        descriptionTextView =
            view.findViewById(R.id.estate_detail_fragment_description_text_textview)
        typeTextView = view.findViewById(R.id.estate_detail_fragment_type_textview)
        informationTitleTextView =
            view.findViewById(R.id.estate_detail_fragment_information_title_textview)
        surfaceTextView = view.findViewById(R.id.estate_detail_fragment_surface_textview)
        numberRoomsTextView = view.findViewById(R.id.estate_detail_fragment_number_rooms_textview)
        numberBedroomsTextView =
            view.findViewById(R.id.estate_detail_fragment_number_bedrooms_textview)
        numberBathroomsTextView =
            view.findViewById(R.id.estate_detail_fragment_number_bathrooms_textview)
        interestTitleTextView =
            view.findViewById(R.id.estate_detail_fragment_interest_title_textview)
        schoolTextView = view.findViewById(R.id.estate_detail_fragment_schools_textview)
        foodTextView = view.findViewById(R.id.estate_detail_fragment_foods_textview)
        shopTextView = view.findViewById(R.id.estate_detail_fragment_shops_textview)
        busTextView = view.findViewById(R.id.estate_detail_fragment_buses_textview)
        tramTextView = view.findViewById(R.id.estate_detail_fragment_tramway_textview)
        parkTextView = view.findViewById(R.id.estate_detail_fragment_park_textview)

        // Initialize adapter with an empty list
        photoAdapter = DetailPhotoPagerAdapter(photoList)

        if (checkPermissions()) {
            viewPagerPhotos.adapter = photoAdapter
        }

        estateViewModel.selectedProperty.observe(viewLifecycleOwner) { property ->
            // Update photolist when property selected changed, update adapter with new list
            photoList = property.photos
            photoAdapter.updateData(photoList)

            // Update informations of property
            titleTextView.text = property.title
            cityTextView.text = property.city
            priceTextView.text = "${property.price}€"  //
            descriptionTextView.text = property.description
            typeTextView.text = property.typeOfProperty
            surfaceTextView.text = "${property.surface} m2"
            numberRoomsTextView.text = "${property.numberOfRooms} rooms"
            numberBedroomsTextView.text = "${property.numberOfBedrooms} bedrooms"
            numberBathroomsTextView.text = "${property.numberOfBathrooms} bathrooms"

            if (property.isNearSchools) {
                schoolTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_school_24,
                    0,
                    0,
                    0
                )
                schoolTextView.compoundDrawableTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }
            if (property.isNearRestaurants) {
                foodTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_food_bank_24,
                    0,
                    0,
                    0
                )
                foodTextView.compoundDrawableTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }
            if (property.isNearShops) {
                shopTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_shopping_cart_24,
                    0,
                    0,
                    0
                )
                shopTextView.compoundDrawableTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }

            if (property.isNearBuses) {
                busTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_directions_bus_24,
                    0,
                    0,
                    0
                )
                busTextView.compoundDrawableTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }

            if (property.isNearTramway) {
                tramTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_tram_24,
                    0,
                    0,
                    0
                )
                tramTextView.compoundDrawableTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }

            if (property.isNearPark) {
                parkTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_park_24,
                    0,
                    0,
                    0
                )
                parkTextView.compoundDrawableTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }


            photoAdapter.onItemClick = { position ->
                val dialog = FullScreenPhotoDialogFragment(photoList, position)
                dialog.show(parentFragmentManager, "FullScreenPhotoDialog")
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_detail, menu)
    }

    private fun checkPermissions(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
        Log.d("EstateDetail", "Checking permission for $permission: $hasPermission")

        return if (hasPermission) {
            true
        } else {
            requestPermissions(arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION)
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            Log.d("EstateDetail", "Permission result for ${permissions[0]}: $granted")

            if (granted) {
                // Permissions granted
                viewPagerPhotos.adapter = photoAdapter
            } else {
                // I'll show a message
            }
        }
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