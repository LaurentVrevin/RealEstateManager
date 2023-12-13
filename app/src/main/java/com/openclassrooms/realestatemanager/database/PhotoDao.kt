package com.openclassrooms.realestatemanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.model.Photo

@Dao
interface PhotoDao {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: List<Photo>)

    @Query("SELECT * FROM photos WHERE propertyId = :propertyId")
    suspend fun getPhotosForProperty(propertyId: String): List<Photo>

    // Modifier le nom d'une photo
    @Query("UPDATE photos SET photoName = :newName WHERE id = :photoId")
    suspend fun updatePhotoName(photoId: String, newName: String)

    // Supprimer une photo par son ID
    @Query("DELETE FROM photos WHERE id = :photoId")
    suspend fun deletePhotoById(photoId: String)*/
}