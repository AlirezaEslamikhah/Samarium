package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.database.NetworkInfoDatabaseHelper

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.myapplication.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var databaseHelper: NetworkInfoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        databaseHelper = NetworkInfoDatabaseHelper(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Fetch data from the database
        val networkInfoList = databaseHelper.getAllInfo()

        // Add markers for each NetworkInfo object
        for (info in networkInfoList) {
            val position = LatLng(info.latitude, info.longitude)
            val color = getColorForSituation(info.situation)
            mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(info.situation)
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
            )
        }

        // Move the camera to a default position (optional)
        if (networkInfoList.isNotEmpty()) {
            val firstInfo = networkInfoList[0]
            val initialPosition = LatLng(firstInfo.latitude, firstInfo.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 10f))
        }
    }
    private fun getColorForSituation(situation: String): Float {
        return when (situation) {
            "very poor" -> BitmapDescriptorFactory.HUE_RED
            "poor" -> BitmapDescriptorFactory.HUE_ORANGE
            "fair" -> BitmapDescriptorFactory.HUE_YELLOW
            "good" -> BitmapDescriptorFactory.HUE_GREEN
            "excellent" -> BitmapDescriptorFactory.HUE_BLUE
            else -> BitmapDescriptorFactory.HUE_VIOLET
        }
    }
}