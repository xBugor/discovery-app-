package com.example.mobileprogramming

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.DirectionsRoute

class Harita : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_harita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment=supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)




    }


    override fun onMapReady(p0: GoogleMap) {
        val eventAddress = intent.getStringExtra("event_address") ?: ""
        val eventname = intent.getStringExtra("event_name") ?: ""
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(eventAddress, 1)


        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            p0?.let { googleMap ->
                var location = latLng // Örnek konum: İstanbul
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(eventname)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
                getUserLocation { userLocation ->
                    if (userLocation != null) {
                        drawRoute(userLocation, latLng)
                    }
                }
            } ?: run {
                Log.e("Harita", "Harita yüklenemedi.")
            }
        }





    }

    private fun getUserLocation(callback: (LatLng?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //BU KISMI OTOMATİK EKLEDİ....
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                callback(LatLng(location.latitude, location.longitude))
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        }
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        // Use the Directions API to get directions
        val context = GeoApiContext.Builder()
            .apiKey("AIzaSyCCNATtaM3SMa-222w-Ttv6Nih_-Dv8NjM") // Add your API Key here
            .build()

        val request = DirectionsApi.newRequest(context)
            .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
            .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
            .mode(com.google.maps.model.TravelMode.DRIVING)

        request.setCallback(object : com.google.maps.PendingResult.Callback<DirectionsResult> {
            override fun onResult(result: DirectionsResult?) {
                if (result != null && result.routes.isNotEmpty()) {
                    val route: DirectionsRoute = result.routes[0]
                    val polylineOptions = PolylineOptions()

                    // Add each step of the route to the Polyline
                    route.legs[0].steps.forEach { step ->
                        polylineOptions.add(LatLng(step.endLocation.lat, step.endLocation.lng))
                    }

                    // Draw the polyline on the map
                    runOnUiThread {
                        googleMap.addPolyline(polylineOptions)
                    }
                }
            }

            override fun onFailure(e: Throwable?) {
                Log.e("Harita", "Failed to get directions: $e")
            }
        })
    }
}
