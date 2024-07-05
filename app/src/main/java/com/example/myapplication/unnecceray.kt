//    private fun startLocationUpdates() {
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this)
//        }
//    }

//    override fun onLocationChanged(location: Location) {
//        locationText.text = "Location: Lat: ${location.latitude}, Long: ${location.longitude}"
//        eventTimeText.text = "Event Time: ${System.currentTimeMillis()}"
//    }

//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
//                (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) &&
//                (grantResults.isNotEmpty() && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
//                startLocationUpdates()
//                displayNetworkInfo()
//            } else {
//
//            }
//        }
//    }

