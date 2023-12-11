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
                        supportActionBar?.show()
                    }
                }
                R.id.nav_mapview_fragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_mapview) {
                        bottomNavigationView.selectedItemId = R.id.nav_mapview
                        supportActionBar?.show()
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)

                    }
                }
                R.id.nav_favorite_fragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_favorite) {
                        bottomNavigationView.selectedItemId = R.id.nav_favorite
                        supportActionBar?.show()
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }
                }
                R.id.nav_loanSimulationFragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_loan_simulator) {
                        bottomNavigationView.selectedItemId = R.id.nav_loan_simulator
                        supportActionBar?.hide()

                    }
                }
                R.id.nav_home_fragment -> {
                    if (bottomNavigationView.selectedItemId != R.id.nav_home) {
                        bottomNavigationView.selectedItemId = R.id.nav_home
                        supportActionBar?.hide()
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
                    if (navController.currentDestination?.id != R.id.nav_mapview_fragment) {
                        navController.navigate(R.id.nav_mapview_fragment)
                    }
                }
                R.id.nav_favorite -> {
                    if (navController.currentDestination?.id != R.id.nav_favorite_fragment) {
                        navController.navigate(R.id.nav_favorite_fragment)
                    }
                }
                R.id.nav_loan_simulator -> {
                    if (navController.currentDestination?.id != R.id.nav_loanHostFragment) {
                        navController.navigate(R.id.nav_loanHostFragment)
                    }
                }

                R.id.nav_home -> {
                    if (navController.currentDestination?.id != R.id.nav_home_fragment) {
                        navController.navigate(R.id.nav_home_fragment)
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




}
