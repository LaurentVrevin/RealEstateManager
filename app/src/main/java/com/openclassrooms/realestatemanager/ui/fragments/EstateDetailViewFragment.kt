package com.openclassrooms.realestatemanager.ui.fragments


import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.ui.adapters.DetailPhotoListAdapter


import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class EstateDetailViewFragment : Fragment(), EasyPermissions.PermissionCallbacks {


    private var isFavorite = false
    private lateinit var estatePhotoRecyclerView: RecyclerView
    private lateinit var photoAdapter: DetailPhotoListAdapter
    private var photoList: List<Photo> = emptyList()
    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 1001
    }

    private val estateViewModel: EstateViewModel by viewModels({ requireActivity() })


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estate_detail_view, container, false)

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize RecyclerView
        estatePhotoRecyclerView = view.findViewById(R.id.recyclerViewPhotos)
        estatePhotoRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Initialize adapter with an empty list
        photoAdapter = DetailPhotoListAdapter(photoList)

        if (checkPermissions()) {
            estatePhotoRecyclerView.adapter = photoAdapter
        }

        estateViewModel.getSelectedProperty().observe(viewLifecycleOwner) { property ->
            // update photolist when property selected changed, update adapter with new list
            photoList = property.photos
            photoAdapter.updateData(photoList)
            Log.d("EstateDetail", "Fragment - Updated data: ${property.photos}")
        }

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_detail, menu)
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // Api Version < 33, ask permission with : READ_EXTERNAL_STORAGE
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    READ_EXTERNAL_STORAGE
                )
            ) {
                true // Permissions granted
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.read_external_storage_rationale),
                    REQUEST_CODE_STORAGE_PERMISSION,
                    READ_EXTERNAL_STORAGE
                )
                false // permissions no granted
            }
        } else {
            // API 33 and up, ask permission with READ_MEDIA_IMAGES
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES

                )
            ) {
                true // permissions granted
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.read_external_storage_rationale),
                    REQUEST_CODE_STORAGE_PERMISSION,
                    Manifest.permission.READ_MEDIA_IMAGES,

                )
                false // permissions no granted
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Pass the results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE

                )
            ) {
                // Permissions granted, attach adapter to the recyclerview
                estatePhotoRecyclerView.adapter = photoAdapter
            } else {
                // Permissions denied, show error message
            }
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // Permissions granted, you can take necessary actions
        estatePhotoRecyclerView.adapter = photoAdapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

            // Request permission again
            checkPermissions()
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