package com.example.myapplication.network

import android.content.Context
import android.os.Build
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityLte
import android.telephony.CellIdentityWcdma
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.myapplication.MainActivity
import com.example.myapplication.database.DatabaseHelper
import android.telephony.TelephonyManager
import android.telephony.CellInfo
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.CellInfoGsm

object NetworkHelper {
    @RequiresApi(Build.VERSION_CODES.P)
    fun displayNetworkInfo(activity: MainActivity, cellTechText: TextView, cellLocationText: TextView, signalQualityText: TextView) {
        val telephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        var _signalStrength = -10000
        var _rsrq = -10000
        var _rsrp = -10000
        var _rscp = -10000
        var _ecNo = -10000
        var _plmnId = " "
        var _rac = " "
        var _tac = " "
        var _lac = " "
        var _cellId = " "

        try {
            val cellTech = when (telephonyManager.networkType) {
                TelephonyManager.NETWORK_TYPE_LTE -> "4G (LTE)"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                TelephonyManager.NETWORK_TYPE_HSPAP -> "3G (HSPA+)"
                TelephonyManager.NETWORK_TYPE_HSPA -> "3G (HSPA)"
                TelephonyManager.NETWORK_TYPE_UMTS -> "3G (UMTS)"
                TelephonyManager.NETWORK_TYPE_EDGE -> "2G (EDGE)"
                TelephonyManager.NETWORK_TYPE_GPRS -> "2G (GPRS)"
                else -> "Unknown"
            }
            cellTechText.text = "Cell Technology: $cellTech"

            val cellInfoList: List<CellInfo> = telephonyManager.allCellInfo
            var cellLocationTextValue = "Cell Location: N/A"
            var signalQualityTextValue = "Signal Quality: N/A"

            for (cellInfo in cellInfoList) {
                when (cellInfo) {
                    is CellInfoLte -> {
                        val cellIdentityLte = cellInfo.cellIdentity as CellIdentityLte
                        val plmnId = cellIdentityLte.mccString + cellIdentityLte.mncString
                        val tac = cellIdentityLte.tac.toString()
                        val cellId = cellIdentityLte.ci.toString()
                        _plmnId = plmnId
                        _tac = tac
                        _cellId = cellId
                        cellLocationTextValue = "Cell Location: PLMN ID: $plmnId, TAC: $tac, Cell ID: $cellId"

                        val cellSignalStrengthLte = cellInfo.cellSignalStrength
                        val rsrq = cellSignalStrengthLte.rsrq
                        val rsrp = cellSignalStrengthLte.rsrp
                        val sinr = cellSignalStrengthLte.rssnr
                        val signalStrength = cellSignalStrengthLte.dbm
                        _rsrq = rsrq
                        _rsrp = rsrp
                        _signalStrength = signalStrength
                        signalQualityTextValue = "Signal Quality: RSRQ: $rsrq, RSRP: $rsrp, SINR: $sinr, Signal Strength: $signalStrength dBm"
                        break
                    }
                    is CellInfoWcdma -> {
                        val cellIdentityWcdma = cellInfo.cellIdentity as CellIdentityWcdma
                        val plmnId = cellIdentityWcdma.mccString + cellIdentityWcdma.mncString
                        val lac = cellIdentityWcdma.lac.toString()
                        val cellId = cellIdentityWcdma.cid.toString()
                        _plmnId = plmnId
                        _lac = lac
                        _cellId = cellId
                        cellLocationTextValue = "Cell Location: PLMN ID: $plmnId, LAC: $lac, Cell ID: $cellId"

                        val cellSignalStrengthWcdma = cellInfo.cellSignalStrength
                        val rscp = cellSignalStrengthWcdma.dbm
                        val signalStrength = cellSignalStrengthWcdma.dbm
                        _signalStrength = signalStrength
                        _rscp = rscp
                        signalQualityTextValue = "Signal Quality: RSCP: $rscp, Signal Strength: $signalStrength dBm"
                        break
                    }
                    is CellInfoGsm -> {
                        val cellIdentityGsm = cellInfo.cellIdentity as CellIdentityGsm
                        val plmnId = cellIdentityGsm.mccString + cellIdentityGsm.mncString
                        val lac = cellIdentityGsm.lac.toString()
                        val cellId = cellIdentityGsm.cid.toString()
                        _plmnId = plmnId
                        _lac = lac
                        _cellId = cellId
                        cellLocationTextValue = "Cell Location: PLMN ID: $plmnId, LAC: $lac, Cell ID: $cellId"

                        val cellSignalStrengthGsm = cellInfo.cellSignalStrength
                        val rssi = cellSignalStrengthGsm.dbm
                        val signalStrength = cellSignalStrengthGsm.dbm
                        _signalStrength = signalStrength
                        signalQualityTextValue = "Signal Quality: RSSI: $rssi, Signal Strength: $signalStrength dBm"
                        break
                    }
                }
            }
            cellLocationText.text = cellLocationTextValue
            signalQualityText.text = signalQualityTextValue

        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        DatabaseHelper.insertNetworkInfoToDatabase(activity, _signalStrength, _rsrq, _rsrp, _rscp, _ecNo, _plmnId, _rac, _tac, _lac, _cellId)
    }
}
