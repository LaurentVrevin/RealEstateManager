package com.openclassrooms.realestatemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.fragments.EstateListViewFragment
import com.openclassrooms.realestatemanager.ui.fragments.MapViewFragment
import com.openclassrooms.realestatemanager.viewmodels.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        drawer = findViewById(R.id.drawer_layout)
        bottomNavigationView = findViewById(R.id.bottom_nav_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )


        // Récupérer la liste LiveData de propriétés
        val propertyListLiveData = estateViewModel.getPropertyList()
        // Observer la liste LiveData pour les mises à jour
        propertyListLiveData.observe(this) { propertyList ->
            Log.d("TESTLIVEDATA", "La liste contient : ${propertyList.size} objets")
            Log.d("TESTLIVEDATA", "Cet objet contient :  $propertyList")
        }
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        configureBottomView()

        // Initialize fragment to display
        displayFragment(EstateListViewFragment())
    }

    private fun configureBottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_listview -> {
                    displayFragment(EstateListViewFragment())

                }

                R.id.nav_mapview -> {
                    displayFragment(MapViewFragment())

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

    // A function to display a fragment
    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
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
