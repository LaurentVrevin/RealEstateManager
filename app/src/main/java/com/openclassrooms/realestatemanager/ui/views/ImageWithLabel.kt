package com.openclassrooms.realestatemanager.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo

class ImageWithLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val labelTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_image_with_label, this, true)

        imageView = findViewById(R.id.imageView)
        labelTextView = findViewById(R.id.labelTextView)
    }

    fun setImage(photo: Photo) {

        if (photo.imageUrl != null) {
            val photoUrl = photo.imageUrl
            Glide.with(this.context)
                .load(photoUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)
        }
    }

    fun setLabelText(text: String?) {
        labelTextView.text = text?:""
    }
}