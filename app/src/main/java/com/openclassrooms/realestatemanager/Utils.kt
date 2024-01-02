package com.openclassrooms.realestatemanager

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Double): Double {
        return Math.round(dollars * 0.812).toDouble()
    }

    //I can do like this, but result is not really the same.
    //Or i can save the original result with dollar and go back to this, i'll see
    fun convertEuroToDollar(euros: Int): Int {
        val exchangeRate = 1.23
        return Math.round(euros * exchangeRate).toInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
            return dateFormat.format(Date())
        }

    val formattedTodayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }
    fun formatCustomDate(date: Date): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatPrice(price: Double): String {
        return if (price % 1.0 == 0.0) {
            String.format("%.0f", price) // no double if number is an integer
        } else {
            price.toString() // with double if must be
        }
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }
    fun isNetworkAvailable(context: Context): Boolean {
        //get a instance of ConnectivityManager from context
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //check if version api 23 or more
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false //if no capacity, return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            //check is network have capabilities of transport for : wifi, phone or ethernet
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            // For previous version to Android M (API 23)
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            //check if network is connected
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}