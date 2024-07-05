package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.telephony.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.database.NetworkInfo
import com.example.myapplication.database.NetworkInfoDatabaseHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import android.os.Looper
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() //, LocationListener// {
{
    private lateinit var locationText: TextView
    private lateinit var eventTimeText: TextView
    private lateinit var cellTechText: TextView
    private lateinit var cellLocationText: TextView
    private lateinit var signalQualityText: TextView
    private lateinit var errorText: TextView


    private lateinit var db: NetworkInfoDatabaseHelper
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun run() {
            getLastLocation()
            displayNetworkInfo()
            handler.postDelayed(this, 60000)
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationText = findViewById(R.id.locationText)
        eventTimeText = findViewById(R.id.eventTimeText)
        cellTechText = findViewById(R.id.cellTechText)
        cellLocationText = findViewById(R.id.cellLocationText)
        signalQualityText = findViewById(R.id.signalQualityText)
//        errorText = findViewById(R.id.error)

        db =  NetworkInfoDatabaseHelper(this)
        val networkInfoList = db.getAllInfo()
        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, networkInfoList.map { it.toString() })
        listView.adapter = adapter


        checkLocationPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        CheckUserPermissions()

        val buttonOpenMap = findViewById<Button>(R.id.btn_open_map)
        buttonOpenMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun CheckUserPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE),
                1)

        } else {
            handler.post(runnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    // Use the latitude and longitude as needed
                    locationText.text = "Location: Lat: ${latitude}, Long: ${longitude}"
//                    eventTimeText.text = "Event Time: ${System.currentTimeMillis()}"
                    // Format the current time
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val eventTime = dateFormat.format(Date(System.currentTimeMillis()))

                    eventTimeText.text = "Event Time: $eventTime"
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure in getting location
                requestLocationUpdates()
            }
    }


    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Use the latitude and longitude as needed
                    locationText.text = "Location: Lat: ${location.latitude}, Long: ${location.longitude}"
//                    eventTimeText.text = "Event Time: ${System.currentTimeMillis()}"
                    // Format the current time
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val eventTime = dateFormat.format(Date(System.currentTimeMillis()))

                    eventTimeText.text = "Event Time: $eventTime"
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                // Permission denied, handle appropriately
            }
        }
    }
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
    private fun displayNetworkInfo() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        var _signalStrength = -10000
        var _rsrq = -10000
        var _rsrp =  -10000
        var _rscp =  -10000
        var _ecNo = -10000
        var _plmnId = " "
        var _rac = " "
        var _tac =  " "
        var _lac =" "
        var _cellId = " "

        try {
            // Cell technology
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

            // Cell location and signal quality
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
                        _signalStrength = signalStrength.toInt()
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
                        _signalStrength = signalStrength.toInt()
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
                        _signalStrength = signalStrength.toInt()
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

        insertNetworkInfoToDatabase(_signalStrength,_rsrq,_rsrp,_rscp , _ecNo , _plmnId , _rac , _tac , _lac , _cellId)
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

    @RequiresApi(Build.VERSION_CODES.P)
    private fun insertNetworkInfoToDatabase(signalStrength: Int?, rsrq: Int?, rsrp: Int?, rscp: Int?, ecNo: Int?, plmnId: String?,rac: String? , tac: String? , lac: String? , cellId:String?) {
        val eventTime = System.currentTimeMillis()
        val location = Location(LocationManager.GPS_PROVIDER) // Assume location is fetched
        var latitude = location.latitude
        var longitude = location.longitude
        val cellTechnology = getCellTechnology() // Assume this method returns the current cell technology

        val situation = calculateSituation(signalStrength)
        val color = getSituationColor(situation)
        if (latitude == 0.0 && longitude == 0.0 )
        {
            latitude = 35.7103361
            longitude = 51.4114904
        }
        val networkInfo = NetworkInfo(
            eventTime = eventTime,
            latitude = latitude,
            longitude = longitude,
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


        db.insertInfo(networkInfo)
    }

    private fun getCellTechnology(): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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
