package com.example.myapplication.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.example.myapplication.MainActivity
import java.text.SimpleDateFormat
import java.util.*

object LocationHelper {
    lateinit var fusedLocationClient: FusedLocationProviderClient

    fun getFusedLocationProviderClient(activity: MainActivity): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(activity)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getLastLocation(activity: MainActivity, locationText: TextView, eventTimeText: TextView) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    locationText.text = "Location: Lat: ${latitude}, Long: ${longitude}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val eventTime = dateFormat.format(Date(System.currentTimeMillis()))
                    eventTimeText.text = "Event Time: $eventTime"
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure in getting location
                requestLocationUpdates(activity, locationText, eventTimeText)
            }
    }

    private fun requestLocationUpdates(activity: MainActivity, locationText: TextView, eventTimeText: TextView) {
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
                    locationText.text = "Location: Lat: ${latitude}, Long: ${longitude}"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val eventTime = dateFormat.format(Date(System.currentTimeMillis()))
                    eventTimeText.text = "Event Time: $eventTime"
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun checkLocationPermission(activity: MainActivity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                activity.LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
}
