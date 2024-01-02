package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetworkAvailableInstrumentedTest {

    @Test
    fun useAppContextToCheckNetworkAvailability() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull("App context should not be null", appContext)

        // Test isNetworkAvailable
        val isNetworkAvailable = Utils.isNetworkAvailable(appContext)
         Assert.assertTrue(isNetworkAvailable)
    }
}