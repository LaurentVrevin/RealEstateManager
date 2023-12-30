package com.openclassrooms.realestatemanager.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.data.model.Property


class EstateListAdapter(private val itemList: List<Property>, isCurrencyEuro:Boolean) : RecyclerView.Adapter<EstateListAdapter.EstateViewHolder>() {

    private var propertyList: List<Property> = emptyList()
    private var onItemClickListener: ((Property) -> Unit)? = null
    private var isCurrencyEuro:Boolean=false

    fun setOnItemClickListener(listener: (Property) -> Unit) {
        onItemClickListener = listener
    }

    //update data to the adapter
    fun updateData(newPropertyList: List<Property>, isCurrencyEuro: Boolean) {
        propertyList = newPropertyList
        this.isCurrencyEuro=isCurrencyEuro
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_estate_fragment_listview, parent, false)
        return EstateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        val currentItem = propertyList[position]
        isCurrencyEuro
        holder.bind(currentItem, isCurrencyEuro)

        // add a click listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleNameTextView: TextView = itemView.findViewById(R.id.item_estate_titlename_textview)
        private val cityNameTextView: TextView = itemView.findViewById(R.id.item_estate_cityname_textview)
        private val priceTextView: TextView = itemView.findViewById(R.id.item_estate_price_textview)
        private val photoImageView: ImageView = itemView.findViewById(R.id.item_estate_photo_imageview)
        private val isSoldTextView: TextView = itemView.findViewById(R.id.item_estate_is_sold_textview)

        // link data from property to view
        fun bind(property: Property, isCurrencyEuro: Boolean) {
            titleNameTextView.text = property.title
            cityNameTextView.text = property.city

            if(isCurrencyEuro){
                priceTextView.text = Utils.formatPrice(property.eurosPrice)+"€"
            }else{
                priceTextView.text=Utils.formatPrice(property.dollarsPrice)+"$"
            }

            val isSold = property.isSold
            if(isSold){
                isSoldTextView.visibility=VISIBLE
            }else{
                isSoldTextView.visibility= GONE
            }

            if (property.photos.isNotEmpty()) {
                val photoUrl = property.photos[0].imageUrl
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