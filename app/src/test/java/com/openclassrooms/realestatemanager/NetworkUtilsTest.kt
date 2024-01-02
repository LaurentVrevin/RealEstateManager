package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetworkUtilsTest {

    @Mock
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Mock
    private lateinit var mockWifiManager: WifiManager

    @Mock
    private lateinit var mockNetwork: Network

    @Mock
    private lateinit var mockNetworkCapabilities: NetworkCapabilities


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun testIsInternetAvailable_whenWifiEnabled() {
        //mockWifiManager return true when iswifienabled is called, wifi actived!
        Mockito.`when`(mockWifiManager.isWifiEnabled).thenReturn(true)
        val context = Mockito.mock(Context::class.java)
        //Simulation of the context to return mockwifimanager when method getsystem service is called
        Mockito.`when`(context.getSystemService(Context.WIFI_SERVICE)).thenReturn(mockWifiManager)
        //check if method return true = wifi actived!
        assertTrue(Utils.isInternetAvailable(context))
    }

    @Test
    fun testIsNetworkAvailable_whenNoActiveNetwork() {
        //mockConnectivityManager return null when activenetwork is called, no network available!
        Mockito.`when`(mockConnectivityManager.activeNetwork).thenReturn(null)

        val context = Mockito.mock(Context::class.java)
        //configure simulate context to return mockConnectivityManager
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)

        assertFalse(Utils.isNetworkAvailable(context))
    }








}