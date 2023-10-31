package com.openclassrooms.realestatemanager.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.openclassrooms.realestatemanager.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            // Open MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}