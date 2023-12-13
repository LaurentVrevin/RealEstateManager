package com.openclassrooms.realestatemanager.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo
import com.openclassrooms.realestatemanager.ui.views.ImageWithLabel

class DetailPhotoPagerAdapter(private var itemPhoto: List<Photo>) :
    RecyclerView.Adapter<DetailPhotoPagerAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = emptyList()

    fun updateData(newPhotoList: List<Photo>) {
        photoList = newPhotoList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_detail_fragment, parent, false)
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
        private val imageWithLabel: ImageWithLabel = itemView.findViewById(R.id.imageviewcustom)



        fun bind(photo: Photo) {
            imageWithLabel.setImage(photo)
            imageWithLabel.setLabelText(photo.photoName)

        }
    }
}