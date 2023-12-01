package com.openclassrooms.realestatemanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Property
import com.openclassrooms.realestatemanager.repositories.EstateItemClickListener
import com.openclassrooms.realestatemanager.ui.fragments.EstateListViewFragment

class EstateListAdapter(private val itemClickListener: EstateItemClickListener) : RecyclerView.Adapter<EstateListAdapter.EstateViewHolder>() {

    private var propertyList: List<Property> = emptyList()

    //update data to the adapter
    fun updateData(newPropertyList: List<Property>) {
        propertyList = newPropertyList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_estate_fragment_listview, parent, false)
        return EstateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        val estate = propertyList[position]
        holder.bind(estate)

        // Ajouter un écouteur de clic à l'élément de la liste
        holder.itemView.setOnClickListener {
            itemClickListener.onEstateItemClick(estate)
        }
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeNameTextView: TextView = itemView.findViewById(R.id.item_estate_typename_textview)
        private val cityNameTextView: TextView = itemView.findViewById(R.id.item_estate_cityname_textview)
        private val priceTextView: TextView = itemView.findViewById(R.id.item_estate_price_textview)
        private val photoImageView: ImageView = itemView.findViewById(R.id.item_estate_photo_imageview)

        // link data from property to view
        fun bind(property: Property) {
            typeNameTextView.text = property.typeOfProperty
            cityNameTextView.text = property.city
            priceTextView.text = property.price

            if (property.photos.isNotEmpty()) {
                val photoUrl = property.photos[0].imageUri?.toString()
                // Load image with glide
                Glide.with(itemView.context)
                    .load(photoUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(photoImageView)


            }
        }
    }
}