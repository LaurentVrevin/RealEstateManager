package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.EstateDatabase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject


class EstateContentProvider :ContentProvider() {

    @Inject
    lateinit var database: EstateDatabase

    companion object {
        private const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        private const val PATH_PROPERTY = "properties"

        private const val CODE_PROPERTY_DIR = 1
        private const val CODE_PROPERTY_ITEM = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_PROPERTY, CODE_PROPERTY_DIR)
            addURI(AUTHORITY, "$PATH_PROPERTY/#", CODE_PROPERTY_ITEM)
        }
    }

    override fun onCreate(): Boolean {
        // Injection HILT
        val appContext = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint = EntryPointAccessors.fromApplication(appContext, ContentProviderEntryPoint::class.java)
        hiltEntryPoint.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = uriMatcher.match(uri)
        if (code == CODE_PROPERTY_DIR || code == CODE_PROPERTY_ITEM) {
            val context = context ?: return null
            val propertyDao = database.propertyDao()
            val cursor: Cursor? = if (code == CODE_PROPERTY_DIR) {
                propertyDao.selectAllPropertiesCursor()
            } else {
                propertyDao.selectPropertyByIdCursor(ContentUris.parseId(uri))
            }
            cursor?.setNotificationUri(context.contentResolver, uri)
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    //I dont need to manage because provider need only reading
    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    @dagger.hilt.EntryPoint
    @dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
    interface ContentProviderEntryPoint {
        fun inject(contentProvider: EstateContentProvider)
    }
}