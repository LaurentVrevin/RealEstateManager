package com.openclassrooms.realestatemanager.ui.activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.fragments.EstateDetailViewFragment
import com.openclassrooms.realestatemanager.ui.fragments.EstateListViewFragment

class MainActivity : AppCompatActivity() {


    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val listFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container_list) as EstateListViewFragment?
        val detailFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container_detail) as EstateDetailViewFragment?


        // Initialize the DrawerLayout
        drawer = findViewById(R.id.drawer_layout)

        // Create the ActionBarDrawerToggle and set it as the drawer listener
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate menu from xml
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.searchIcon -> {

                return true
            }
            R.id.editIcon -> {
                val intent = Intent(this, EstateDetailEditActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.addIcon -> {

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}