package com.example.mobileprogramming

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
        // Harita nesnesinin null olmadığını kontrol edin
        map?.let { googleMap ->
            val location = LatLng(41.0082, 28.9784) // Örnek konum: İstanbul
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Marker in Istanbul")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        } ?: run {
            // Eğer harita null ise bir hata mesajı veya log eklenebilir
            Log.e("Harita", "Harita yüklenemedi.")
        }
    }}
