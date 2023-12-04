package com.openclassrooms.realestatemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    private lateinit var bottomNavigationView: BottomNavigationView

    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Initialize the nav host fragment as the container with a controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        setupActionBarWithNavController(navController)


        bottomNavigationView = findViewById(R.id.bottom_nav_view)

        configureBottomView(navController)

        // Add listener for destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Update the selected item in BottomNavigationView based on the current destination
            when (destination.id) {
                R.id.nav_listview_fragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_listview) {
                        bottomNavigationView.selectedItemId = R.id.nav_listview
                    }
                }

                R.id.nav_mapViewFragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_mapview) {
                        bottomNavigationView.selectedItemId = R.id.nav_mapview
                    }
                }
            }
        }


        // Get back the list with livedata (property)
        val propertyListLiveData = estateViewModel.getPropertyList()

        // Observe list with livedata about updates
        propertyListLiveData.observe(this) { propertyList ->
            Log.d("TESTLIVEDATA", "La liste contient : ${propertyList.size} objets")
        }
    }


    //HANDLES THE BACKWARD OF FRAGMENTS
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun configureBottomView(navController: NavController) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_listview -> {
                    if (navController.currentDestination?.id != R.id.nav_listview_fragment) {
                        navController.navigate(R.id.nav_listview_fragment)
                    }
                }

                R.id.nav_mapview -> {
                    if (navController.currentDestination?.id != R.id.nav_mapViewFragment) {
                        navController.navigate(R.id.nav_mapViewFragment)
                    }
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addIcon -> {
                // Open AddEstateActivity
                val intent = Intent(this, AddEstateActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    //A function to display a fragment
    /*private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }*/

    override fun onResume() {
        super.onResume()
        //displayFragment(EstateListViewFragment())
        Log.d("ETAT", "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("ETAT", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ETAT", "onStop")
    }



}
