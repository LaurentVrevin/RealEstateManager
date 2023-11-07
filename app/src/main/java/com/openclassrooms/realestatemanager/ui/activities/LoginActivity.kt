package com.openclassrooms.realestatemanager.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R


class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI elements
        val emailEditText = findViewById<TextInputEditText>(R.id.til_price_of_property_edit)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password_textinput_edittext)
        val loginButton = findViewById<Button>(R.id.login_button)
        val cardView = findViewById<CardView>(R.id.cardview1)
        val logoImageView = findViewById<ImageView>(R.id.login_logo_imv)

        // Load animations for elements
        val slideInFromBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
        val slideInFromTopAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_top)


        // Apply animations to elements
        cardView.startAnimation(slideInFromBottomAnimation)
        logoImageView.startAnimation(slideInFromTopAnimation)


        // Open MainActivity on click
        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
