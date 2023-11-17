package com.openclassrooms.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.model.Photo

class AddEstatePhotoAdapter(private val photoMutableList: MutableList<Photo>) : RecyclerView.Adapter<AddEstatePhotoAdapter.ViewHolder>() {

    // Create a new view (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of the view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoMutableList[position]
        holder.imageViewPhoto.setImageURI(photo.imageUri)
        holder.textViewPhotoName.text = photo.photoName

        // Add an OnClickListener to modify the name
        holder.textViewPhotoName.setOnClickListener {

        }
        holder.editButton.setOnClickListener {
            showEditNameDialog(photo, holder.itemView.context)
        }

        // Add an OnClickListener to delete the photo
        holder.deleteButton.setOnClickListener {
            showDeletePhotoDialog(photo, holder.adapterPosition, holder.deleteButton)
        }
    }

    override fun getItemCount(): Int {
        return photoMutableList.size
    }

    // ViewHolder class representing each item in the RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewPhotoName: TextView = itemView.findViewById(R.id.textViewPhotoName)
        val imageViewPhoto: ImageView = itemView.findViewById(R.id.imageViewPhoto)
        val editButton: ImageView = itemView.findViewById(R.id.imageViewEdit)
        val deleteButton: ImageView = itemView.findViewById(R.id.imageViewDelete)
    }

    // Method to display the dialog for editing the photo name
    @SuppressLint("MissingInflatedId")
    private fun showEditNameDialog(photo: Photo, context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_photo_name, null)
        val editTextPhotoName: EditText = dialogView.findViewById(R.id.dialog_edit_photo_name_editTextPhotoNameEdit)
        editTextPhotoName.setText(photo.photoName)

        MaterialAlertDialogBuilder(context, R.style.MyAlertDialogStyle)
            .setTitle(R.string.add_estate_photo_adapter_dialog_edit_title)
            .setView(dialogView)
            .setPositiveButton(R.string.add_estate_photo_adapter_dialog_edit_validate_button) { dialog, _ ->
                val newPhotoName = editTextPhotoName.text.toString()
                if (newPhotoName.isNotEmpty()) {
                    // Update the photo name in the list
                    photo.photoName = newPhotoName
                    notifyDataSetChanged()
                } else {
                    // Show an error message if the name is empty
                    showDialog(R.string.add_estate_photo_adapter_dialog_edit_error_title, R.string.add_estate_photo_adapter_dialog_edit_error_message, context)
                }

                dialog.dismiss()
            }
            .setNegativeButton(R.string.add_estate_photo_adapter_dialog_edit_cancel_button) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialog(title: Int, message: Int, context: Context) {
        MaterialAlertDialogBuilder(context, R.style.MyAlertDialogStyle)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.add_estate_photo_adapter_dialog_edit_ok_button, null)
            .show()
    }

    // Add a method to display the delete dialog
    private fun showDeletePhotoDialog(photo: Photo, position: Int, deleteButton: ImageView) {
        val context = deleteButton.context
        MaterialAlertDialogBuilder(context, R.style.MyAlertDialogStyle)
            .setTitle(R.string.add_estate_photo_adapter_dialog_delete_title)
            .setMessage(R.string.add_estate_photo_adapter_dialog_delete_message)
            .setPositiveButton(R.string.add_estate_photo_adapter_dialog_delete_yes_button) { dialog, _ ->
                // Remove the photo from the list and update the adapter
                photoMutableList.removeAt(position)
                notifyDataSetChanged()
                dialog.dismiss()
                Log.d("TAG", "${photoMutableList.size}")
            }
            .setNegativeButton(R.string.add_estate_photo_adapter_dialog_delete_no_button) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
