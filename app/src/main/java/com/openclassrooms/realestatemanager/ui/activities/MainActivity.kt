package com.openclassrooms.realestatemanager.ui.activities

import android.annotation.SuppressLint
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
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.ui.fragments.EstateListViewFragment
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EstateListViewFragment.OnSearchButtonClickListener {

    private var isCurrencyEuros = false
    private lateinit var propertyListDataMainActivity:List<Property>

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView


    private val estateViewModel: EstateViewModel by viewModels()

    private val isTablet: Boolean by lazy {
        resources.getBoolean(R.bool.isTablet)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        propertyListDataMainActivity = emptyList()
        estateViewModel.propertyList.observe(this) { propertyListLiveData ->
        propertyListDataMainActivity = propertyListLiveData
        }
        if (isTablet) {
            if(propertyListDataMainActivity.isEmpty()){
                Log.d("ISTABLET", "on est en mode tablette, la liste est vide")
            }else{
                Log.d("ISTABLET", "on est en mode tablette, la liste est $propertyListDataMainActivity")
            }
        } else {
            // Configuration standard pour smartphone
            Log.d("ISTABLET", "on est en mode smartphone")
        }

        //Initialize the nav host fragment as the container with a controller
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_nav_view)
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)


        configureBottomView(navController)

        // Add listener for destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Update the selected item in BottomNavigationView based on the current destination
            if (isTablet) {
                // Logic for tablet
                when (destination.id) {
                    R.id.nav_tablet_home_fragment -> {
                        // Handle tablet home fragment specific logic
                        bottomNavigationView.selectedItemId = R.id.nav_listview
                        supportActionBar?.show()
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }
                    R.id.nav_mapview_fragment -> {
                        if (bottomNavigationView.selectedItemId != R.id.nav_mapview) {
                            bottomNavigationView.selectedItemId = R.id.nav_mapview
                            supportActionBar?.show()
                            supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        }
                    }
                    R.id.nav_loan_host_fragment -> {
                        if (bottomNavigationView.selectedItemId != R.id.nav_loan_simulator) {
                            bottomNavigationView.selectedItemId = R.id.nav_loan_simulator
                            supportActionBar?.hide()
                            supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        }
                    }
                }
            } else {
                // Logic for smartphone
                when (destination.id) {
                    R.id.nav_listview_fragment -> {
                        if (bottomNavigationView.selectedItemId != R.id.nav_listview) {
                            bottomNavigationView.selectedItemId = R.id.nav_listview
                            supportActionBar?.show()
                            supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        }
                    }
                    R.id.nav_mapview_fragment -> {
                        if (bottomNavigationView.selectedItemId != R.id.nav_mapview) {
                            bottomNavigationView.selectedItemId = R.id.nav_mapview
                            supportActionBar?.show()
                            supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        }
                    }
                    R.id.nav_loan_host_fragment -> {
                        if (bottomNavigationView.selectedItemId != R.id.nav_loan_simulator) {
                            bottomNavigationView.selectedItemId = R.id.nav_loan_simulator
                            supportActionBar?.hide()
                            supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        }
                    }
                }
            }
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
                    if (isTablet) {
                        if (navController.currentDestination?.id != R.id.nav_tablet_home_fragment) {
                            navController.navigate(R.id.nav_tablet_home_fragment)
                        }
                    } else {
                        if (navController.currentDestination?.id != R.id.nav_listview_fragment) {
                            navController.navigate(R.id.nav_listview_fragment)
                        }
                    }
                }

                R.id.nav_mapview -> {
                    if (navController.currentDestination?.id != R.id.nav_mapview_fragment) {
                        navController.navigate(R.id.nav_mapview_fragment)
                    }
                }

                R.id.nav_loan_simulator -> {
                    if (navController.currentDestination?.id != R.id.nav_loan_host_fragment) {
                        navController.navigate(R.id.nav_loan_host_fragment)
                    }
                }

            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.CurrencyIcon -> {
                // setup the click for favorite
                toggleCurrency(item)
                return true
            }
            R.id.addIcon -> {
                // Open AddEstateActivity
                val intent = Intent(this, AddEstateActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun toggleCurrency(item: MenuItem) {
        //If is favorite so delete favorite, or do favorite
        if (isCurrencyEuros) {
            item.setIcon(R.drawable.baseline_euro_24)
            isCurrencyEuros = false
        } else {

            item.setIcon(R.drawable.baseline_dollar_24)
            isCurrencyEuros = true


        }
    }

    override fun onSearchButtonClick() {
    }

}
