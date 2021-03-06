package com.emove.emove.activities

import android.Manifest
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.emove.emove.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import com.emove.emove.model.SearchResult
import com.emove.emove.model.UserPoint
import com.emove.emove.storage.StorageController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupLocationListener()
        setupLocation()
    }

    private fun setupLocationListener() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...
                }
            }
        }
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

        val emag = LatLng(44.489118, 26.125265)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(emag))
        mMap.animateCamera(CameraUpdateFactory.zoomTo( 12.0f ))

        val lastSearchResult = StorageController.getLastSearchResult()
        lastSearchResult?. let { drawResult(googleMap, it) }
    }

    private val DOT = Dot()
    private val GAP = Gap(20f)

    fun drawResult(googleMap: GoogleMap, searchResult: SearchResult) {

        searchResult.points?.forEach { userPoint: UserPoint ->
            val latLng = LatLng(userPoint.lat, userPoint.lng)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions)
        }

        val overview_polyline_initial = searchResult.initial.directions.overview_polyline
        overview_polyline_initial?.let { addPath(googleMap, PolyUtil.decode(overview_polyline_initial.points), null) }

        val overview_polyline_updated = searchResult.updated.directions.overview_polyline
        overview_polyline_updated?.let { addPath(googleMap, PolyUtil.decode(overview_polyline_updated.points), Arrays.asList(GAP, DOT)) }

    }

    private fun addPath(googleMap: GoogleMap, decoded: List<LatLng>, pattern: MutableList<PatternItem>?) {
        // Add a polyline to the map.
        val polyline1 = googleMap.addPolyline(PolylineOptions()
                .pattern(pattern)
//                .clickable(true)
                .addAll(decoded))

//        // Set listeners for click events.
//        googleMap.setOnPolylineClickListener(this)
//        googleMap.setOnPolygonClickListener(this)

        val builder = LatLngBounds.Builder()
        builder.include(decoded[0])
        builder.include(decoded[decoded.size - 1])
        val bounds = builder.build()

        val padding = 100 // padding around start and end marker
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
//        googleMap.animateCamera(cu)
    }

    fun setupLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission has been granted, continue as usual
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                    }
        }

    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest()
            locationRequest.interval = 1000
            locationRequest.fastestInterval = 1000
            locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null /* Looper */)
        }
    }
}
