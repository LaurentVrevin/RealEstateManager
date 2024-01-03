package com.openclassrooms.realestatemanager.ui.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
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
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.ui.activities.AddEstateActivity
import com.openclassrooms.realestatemanager.ui.adapters.DetailPhotoPagerAdapter


import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstateDetailViewFragment : Fragment(), OnMapReadyCallback {


    private var isCurrencyEuros = false
    private var propertyIsSold: Boolean = false
    private var propertyDateSold:String=""
    private lateinit var viewPagerPhotos: ViewPager2
    private lateinit var photoAdapter: DetailPhotoPagerAdapter
    private lateinit var titleTextView: TextView
    private lateinit var isSoldTextView: TextView
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
    private lateinit var addressTextView: TextView
    private lateinit var addressCityTextView: TextView
    private lateinit var countryTextView: TextView
    private var photoList: List<Photo> = emptyList()

    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null

    companion object {


        private const val REQUEST_CODE_STORAGE_PERMISSION = 1001
    }
    private var isViewCreated = false

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estate_detail_view, container, false)

        val isTablet: Boolean by lazy {
            resources.getBoolean(R.bool.isTablet)
        }
        if (isTablet){
            setHasOptionsMenu(true)
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }else{

            setHasOptionsMenu(true)
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        return view
    }

    @SuppressLint("SetTextI18n", "UseCompatTextViewDrawableApis")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewPager
        isViewCreated = true


        viewPagerPhotos = view.findViewById(R.id.estate_detail_fragment_viewpager_photos)
        titleTextView = view.findViewById(R.id.estate_detail_fragment_title_textview)
        isSoldTextView = view.findViewById(R.id.estate_detail_fragment_is_sold_textview)
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
        addressTextView = view.findViewById(R.id.estate_detail_fragment_address_of_property)
        addressCityTextView = view.findViewById(R.id.estate_detail_fragment_city_of_property)
        countryTextView = view.findViewById(R.id.estate_detail_fragment_country_of_property)


        // Initialize adapter with an empty list
        photoAdapter = DetailPhotoPagerAdapter(photoList)

        if (checkPermissions()) {
            viewPagerPhotos.adapter = photoAdapter
        }
        initDataFromViewModel()
        initMap()

    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDataFromViewModel() {
        estateViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
            val isCurrencyEuro = currency == EstateViewModel.Currency.EUR

            estateViewModel.selectedProperty.observe(viewLifecycleOwner) { property ->
                // Update photolist when property selected changed, update adapter with new list
                photoList = property.photos
                photoAdapter.updateData(photoList)

                // Update informations of property
                titleTextView.text = property.title
                cityTextView.text = property.city

                descriptionTextView.text = property.description
                typeTextView.text = property.typeOfProperty
                surfaceTextView.text = Utils.formatPrice(property.surface) + "m2"
                val displayedPrice = if (isCurrencyEuro) {
                    property.eurosPrice
                } else {
                    property.dollarsPrice
                }
                priceTextView.text = Utils.formatPrice(displayedPrice) + if (isCurrencyEuro) "€" else "$"
                numberRoomsTextView.text = "${property.numberOfRooms} rooms"
                numberBedroomsTextView.text = "${property.numberOfBedrooms} bedrooms"
                numberBathroomsTextView.text = "${property.numberOfBathrooms} bathrooms"
                addressTextView.text = property.address
                addressCityTextView.text = property.city
                countryTextView.text = property.country
                // Check if the property is sold
                propertyIsSold = property.isSold
                propertyDateSold = property.dateSold

                //VISIBILITY FOR PROPERTY TO SELL OR SOLD
                if (propertyIsSold) {
                    isSoldTextView.visibility = VISIBLE
                    isSoldTextView.text =
                        getString(R.string.estate_detail_fragment_is_sold_textview) + propertyDateSold + " by " + property.agentId
                } else {
                    isSoldTextView.visibility = GONE
                }


                if (property.isNearSchools) {
                    schoolTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.baseline_school_24,
                        0,
                        0,
                        0
                    )
                    schoolTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.gold)
                } else {
                    schoolTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
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
                } else {
                    foodTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
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
                } else {
                    shopTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
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
                } else {
                    busTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
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
                } else {
                    tramTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
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
                } else {
                    parkTextView.compoundDrawableTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
                }


                photoAdapter.onItemClick = { position ->
                    val dialog = FullScreenPhotoDialogFragment(photoList, position)
                    dialog.show(parentFragmentManager, "FullScreenPhotoDialog")
                }
                Log.d(
                    "CHECKBOX",
                    "état des checkbox ${property.isNearBuses}, ${property.isNearPark}, ${property.isNearRestaurants}, ${property.isNearSchools}, ${property.isNearTramway}, ${property.isNearShops}"
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_detail, menu)
        // Vérifier si la liste des propriétés est vide
        updateEditIconVisibility(menu)
    }
    private fun updateEditIconVisibility(menu: Menu) {
        estateViewModel.propertyList.observe(viewLifecycleOwner) { properties ->
            val editMenuItem = menu.findItem(R.id.editIcon)
            editMenuItem.isVisible = properties.isNotEmpty()
        }
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
                Toast.makeText(requireContext(), getString(R.string.estate_detail_fragment_no_permission_to_photos_from_device), Toast.LENGTH_LONG).show()
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.editIcon -> {
                if(propertyIsSold){
                    Toast.makeText(requireContext(), "You cant edit this property because she's sold, sorry", Toast.LENGTH_SHORT).show()

                } else {
                    val intent = Intent(activity, AddEstateActivity::class.java)
                    intent.putExtra("PROPERTY_ID", estateViewModel.selectedProperty.value?.id)
                    startActivity(intent)

                }
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    //INIT MAP AND LOCATION ADDRESS
    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.estate_detail_fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // called when map is ready to use

        if (isViewCreated) { //Check if view is on
            estateViewModel.selectedProperty.observe(viewLifecycleOwner) { property ->
                // Position on map
                val location = LatLng(property.latitude, property.longitude)
                googleMap?.addMarker(MarkerOptions().position(location).title(property.title))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        initDataFromViewModel()
        initMap()
    }

    override fun onPause() {
        super.onPause()
        Log.d("CYLEFRAGMENT", "onpause")
    }

    override fun onStart() {
        super.onStart()
        Log.d("CYLEFRAGMENT", "onstart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CYLEFRAGMENT", "ondestroy")
        isViewCreated = false
    }
}