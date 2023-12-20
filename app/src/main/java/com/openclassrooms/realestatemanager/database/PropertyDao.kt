package com.openclassrooms.realestatemanager.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.data.model.Property

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Query("SELECT * FROM properties")
    fun getAllProperties(): LiveData<List<Property>>

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: String): LiveData<Property>

    @Update
    suspend fun updateProperty(property: Property)

    @RawQuery(observedEntities = [Property::class])
    fun searchProperties(query: SupportSQLiteQuery): LiveData<List<Property>>



}