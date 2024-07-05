package com.example.myapplication.database

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.example.myapplication.MainActivity



object DatabaseHelper {

    @RequiresApi(Build.VERSION_CODES.P)
    fun insertNetworkInfoToDatabase(activity: MainActivity, signalStrength: Int?, rsrq: Int?, rsrp: Int?, rscp: Int?, ecNo: Int?, plmnId: String?, rac: String?, tac: String?, lac: String?, cellId: String?) {
        val eventTime = System.currentTimeMillis()
        val location = Location(LocationManager.GPS_PROVIDER)
        var latitude = location.latitude
        var longitude = location.longitude
        val cellTechnology = getCellTechnology(activity)

        val situation = calculateSituation(signalStrength)
        val color = getSituationColor(situation)
//        if (latitude == 0.0 && longitude == 0.0) {
//            latitude = 35.7103361
//            longitude = 51.4114904
//        }

        val networkInfo = NetworkInfo(
            eventTime = eventTime,
            latitude = MainActivity.latitude_main,
            longitude = MainActivity.longitude_main,
            cellTechnology = cellTechnology,
            plmnId = plmnId,
            rac = rac,
            tac = tac,
            lac = lac,
            cellId = cellId,
            signalStrength = signalStrength,
            rsrq = rsrq,
            rsrp = rsrp,
            rscp = rscp,
            ecNo = ecNo,
            signalQuality = "Signal Strength: $signalStrength dBm, RSRQ: $rsrq, RSRP: $rsrp, RSCP: $rscp, EC/No: $ecNo",
            situation = "$situation ($color)"
        )

        val db = NetworkInfoDatabaseHelper(activity)
        db.insertInfo(networkInfo)
    }

    private fun calculateSituation(signalStrength: Int?): String {
        return when (signalStrength) {
            null -> "Unknown"
            10000 -> "Unknown"
            in -85..Int.MAX_VALUE -> "Excellent"
            in -95..-86 -> "Good"
            in -105..-96 -> "Fair"
            in -115..-106 -> "Poor"
            else -> "Very Poor"
        }
    }

    private fun getSituationColor(situation: String): String {
        return when (situation) {
            "Excellent" -> "navy green"
            "Good" -> "green"
            "Fair" -> "yellow"
            "Poor" -> "pink"
            "Very Poor" -> "navy red"
            else -> "unknown"
        }
    }

    private fun getCellTechnology(activity: MainActivity): String {
        val telephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try {
            when (telephonyManager.networkType) {
                TelephonyManager.NETWORK_TYPE_LTE -> "4G (LTE)"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                TelephonyManager.NETWORK_TYPE_HSPAP -> "3G (HSPA+)"
                TelephonyManager.NETWORK_TYPE_HSPA -> "3G (HSPA)"
                TelephonyManager.NETWORK_TYPE_UMTS -> "3G (UMTS)"
                TelephonyManager.NETWORK_TYPE_EDGE -> "2G (EDGE)"
                TelephonyManager.NETWORK_TYPE_GPRS -> "2G (GPRS)"
                else -> "Unknown"
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            "Permission Denied"
        }
    }
}
