package com.example.mobileprogramming

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Harita : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_harita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mapFragment=supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)




    }

    override fun onMapReady(map: GoogleMap?) {
        val eventAddress = intent.getStringExtra("event_address") ?: ""
        val eventname = intent.getStringExtra("event_name") ?: ""
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(eventAddress, 1)


        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            map?.let { googleMap ->
                var location = latLng // Örnek konum: İstanbul
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(eventname)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
            } ?: run {
                // Eğer harita null ise bir hata mesajı veya log eklenebilir
                Log.e("Harita", "Harita yüklenemedi.")
            }
        }






        // Harita nesnesinin null olmadığını kontrol edin

    }}
