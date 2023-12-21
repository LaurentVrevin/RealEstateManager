package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContentProviderInstrumentedTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testQueryAllProperties() {
        //query the URI that matches all properties in the database
        val uri = Uri.parse("content://com.openclassrooms.realestatemanager.provider/properties")
        //ensures that the ContentProvider returns a Cursor
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        assertNotNull(cursor)
        //then checks if the Cursor contains data by moving the pointer to the first row of results
        assertTrue(cursor!!.moveToNext())
        cursor.close()
    }
}