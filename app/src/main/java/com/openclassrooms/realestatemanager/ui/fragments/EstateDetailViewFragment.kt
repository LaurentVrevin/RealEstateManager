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
class EstateDetailViewFragment : Fragment() {


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
            Log.d("EstateDetail", "Observed property: ${property.description}, Photos count: ${property.photos.size}")
            photoAdapter.updateData(photoList)

        }

        return view
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
        val hasPermission = ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
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
                estatePhotoRecyclerView.adapter = photoAdapter
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