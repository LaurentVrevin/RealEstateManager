package com.openclassrooms.realestatemanager.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.data.model.Property

class EstateDetailPhotoAdapter : RecyclerView.Adapter<EstateDetailPhotoAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = emptyList()

    // Mettez à jour les données de la liste des photos
    fun updateData(newPhotoList: List<Photo>) {
        photoList = newPhotoList
        notifyDataSetChanged()
        Log.d("EstateDetail", "Adapter Updated data: $photoList")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_detail_fragment, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewPhoto: ImageView = itemView.findViewById(R.id.item_photo_detail_fragment_imageViewPhoto)
        private val textViewPhotoName: TextView = itemView.findViewById(R.id.item_photo_detail_fragment_textViewPhotoName)

        fun bind(photo: Photo) {
            // Charger l'image avec Glide
            Glide.with(itemView.context)
                .load(photo.imageUri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageViewPhoto)

            textViewPhotoName.text = photo.photoName ?: ""
        }
    }
}