package com.openclassrooms.realestatemanager.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.data.model.Photo

class Converters {
    @TypeConverter
    fun fromPhotoList(photos: List<Photo>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.toJson(photos, type)
    }

    @TypeConverter
    fun toPhotoList(photosString: String): List<Photo> {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(photosString, type)
    }
}