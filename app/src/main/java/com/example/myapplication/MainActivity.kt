package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import com.example.myapplication.database.NetworkInfoDatabaseHelper
import com.example.myapplication.location.LocationHelper
import com.example.myapplication.network.NetworkHelper
import com.example.myapplication.database.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var locationText: TextView
    private lateinit var eventTimeText: TextView
    private lateinit var cellTechText: TextView
    private lateinit var cellLocationText: TextView
    private lateinit var signalQualityText: TextView
    private lateinit var errorText: TextView
    private lateinit var stopInsertionButton: Button

    private lateinit var db: NetworkInfoDatabaseHelper
    val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val handler = Handler(Looper.getMainLooper())
    private var isInsertionStopped = false



    private val runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun run() {
            if (!isInsertionStopped) {
                LocationHelper.getLastLocation(this@MainActivity, locationText, eventTimeText)
                NetworkHelper.displayNetworkInfo(this@MainActivity, cellTechText, cellLocationText, signalQualityText)
            }
            handler.postDelayed(this, 1000)
        }
    }
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        public var longitude_main: Double = 35.7104365
        public var latitude_main: Double = 51.411407
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
        stopInsertionButton = findViewById(R.id.btn_stop_insertion)
        val buttonOpenMap = findViewById<Button>(R.id.btn_open_map)


        db = NetworkInfoDatabaseHelper(this)
        val networkInfoList = db.getAllInfo()
        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, networkInfoList.map { it.toString() })
        listView.adapter = adapter

        LocationHelper.checkLocationPermission(this)
        LocationHelper.fusedLocationClient = LocationHelper.getFusedLocationProviderClient(this)
        checkUserPermissions()


        buttonOpenMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        stopInsertionButton.setOnClickListener {
            isInsertionStopped = !isInsertionStopped
            if (isInsertionStopped) {
                stopInsertionButton.text = "Resume Insertion"
            } else {
                stopInsertionButton.text = "Stop Insertion"
            }
        }


    }

    private fun checkUserPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE),
                1)
        } else {
            handler.post(runnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                LocationHelper.getLastLocation(this, locationText, eventTimeText)
//            } else {
//                // Permission denied, handle appropriately
//            }
//        }
//    }
@RequiresApi(Build.VERSION_CODES.P)
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            // Permission was granted, proceed with accessing location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationHelper.getLastLocation(this, locationText, eventTimeText)
            }
        } else {
            // Permission denied, show a message to the user
            locationText.text = "Location permission denied"
            eventTimeText.text = ""
        }
    }
}
}
