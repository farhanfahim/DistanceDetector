package com.example.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_maps.*
import com.example.distancedectector.RunTimePermissions
import com.example.distancedectector.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var lat: Double = 0.0
    var lng: Double = 1.0
    var dis: Double = 20.0
    var etDis: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        RunTimePermissions.verifyStoragePermissions(this)

        DistanceDetector.startService(this)

        btnCurrentLocation.setOnClickListener {

            if (etDis.isEmpty()) {
                etDis = 20.0.toString()
                DistanceDetector.setUserDistance(etDis.toDouble())
            } else {
                etDis = etDistance.text.toString()
                DistanceDetector.setUserDistance(etDis.toDouble())
            }

            lat = DistanceDetector.getLatitude()
            lng = DistanceDetector.getLongitude()
            dis = DistanceDetector.getUserDistance()

            mapLocation(mMap, lat, lng, dis)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mapLocation(
            googleMap,
            DistanceDetector.getLatitude(),
            DistanceDetector.getLongitude(), dis
        )
    }

    private fun mapLocation(googleMap: GoogleMap, lat: Double, lng: Double, distance: Double) {
        mMap = googleMap

        val latLng = LatLng(lat, lng)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        googleMap.clear()
        googleMap.addMarker(markerOptions)
        val midLatLng: LatLng = googleMap.cameraPosition.target

        googleMap.addCircle(
            CircleOptions()
                .center(midLatLng)
                .radius(distance)
                .strokeWidth(1f)
                .fillColor(R.color.colorPrimary)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        DistanceDetector.stopService(this)
    }


}

