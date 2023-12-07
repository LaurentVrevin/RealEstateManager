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

class DetailPhotoListAdapter(private val itemList: List<Photo>) : RecyclerView.Adapter<DetailPhotoListAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = emptyList()

    fun updateData(newPhotoList: List<Photo>) {
        photoList = newPhotoList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_detail_fragment, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = photoList[position]
        holder.bind(currentPhoto)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewPhoto: ImageView = itemView.findViewById(R.id.item_photo_detail_fragment_imageViewPhoto)
        private val textViewPhotoName: TextView = itemView.findViewById(R.id.item_photo_detail_fragment_textViewPhotoName)

        fun bind(photo: Photo) {
            textViewPhotoName.text = photo.photoName
            if(photo.imageUrl != null){
                val photoUrl = photo.imageUrl
                Log.d("EstateDetail", "estate detail adapter :url : $photoUrl")
                Glide.with(itemView.context)

                    .load(photoUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageViewPhoto)
            }
        }
    }
}