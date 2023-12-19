package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.wifi.WifiManager
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
    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812).toInt()
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
}