package com.openclassrooms.realestatemanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Property

class EstateListAdapter : RecyclerView.Adapter<EstateListAdapter.EstateViewHolder>() {

    private var propertyList: List<Property> = emptyList()

    // Mettez à jour les données de l'adapter avec la liste fournie
    fun updateData(newPropertyList: List<Property>) {
        propertyList = newPropertyList
        notifyDataSetChanged()
    }

    // Créez un ViewHolder pour chaque élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_estate, parent, false)
        return EstateViewHolder(itemView)
    }

    // Liez les données de l'Estate à la vue dans le ViewHolder
    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        val estate = propertyList[position]
        holder.bind(estate)
    }

    // Retournez le nombre total d'éléments dans la liste
    override fun getItemCount(): Int {
        return propertyList.size
    }

    // Déclarez le ViewHolder avec les éléments de la vue à lier
    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeNameTextView: TextView = itemView.findViewById(R.id.item_estate_typename_textview)
        private val cityNameTextView: TextView = itemView.findViewById(R.id.item_estate_cityname_textview)
        private val priceTextView: TextView = itemView.findViewById(R.id.item_estate_price_textview)
        private val photoImageView: ImageView = itemView.findViewById(R.id.item_estate_photo_imageview)

        // Liez les données de l'Estate aux éléments de la vue
        fun bind(property: Property) {
            typeNameTextView.text = property.typeOfProperty
            cityNameTextView.text = property.address
            priceTextView.text = property.price

            if (property.photos.isNotEmpty()) {
                val photoUrl = property.photos[0].imageUri?.toString()
                // Chargez l'image avec Glide
                Glide.with(itemView.context)
                    .load(photoUrl) // Remplacez par la méthode qui vous donne l'URL de l'image
                    .placeholder(R.drawable.placeholder_image) // Placeholder à afficher en attendant le chargement
                    .error(R.drawable.error_image) // Image à afficher en cas d'erreur de chargement*/
                    .into(photoImageView)

                // Ajoutez d'autres liaisons de données au besoin
            }
        }
    }
}