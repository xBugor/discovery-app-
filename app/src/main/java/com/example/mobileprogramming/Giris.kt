package com.example.mobileprogramming
import android.Manifest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import android.location.Location

import com.google.android.material.navigation.NavigationView

class Giris : AppCompatActivity() {
    lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOCATION_PERMISSION_REQUEST_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
         lateinit var drawerLayout: DrawerLayout
         lateinit var navigationView: NavigationView

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission()) {
            getUserLocation()
        } else {
            requestLocationPermission()
        }



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_giris)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        drawerLayout=findViewById(R.id.main)
        var menubutton=findViewById<ImageButton>(R.id.menu)
        navigationView=findViewById(R.id.cekmece)


        menubutton.setOnClickListener(){
            drawerLayout.open()
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Eposta ->{
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.navgorus ->{
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.navayarlar ->{
                    val intent = Intent(this, Ayarlar::class.java)
                    startActivity(intent)

                }
                R.id.navcikis ->{
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()

                }
            }

            // Tıklamadan sonra çekmeceyi kapat
            drawerLayout.closeDrawers()
            true
        }

    }

//izin bilgisinin olup olmadığının kontrolü
    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
//konum bilgisi alma talabi.
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildi, konumu al
                getUserLocation()
            } else {
                // İzin reddedildi
                Toast.makeText(this, "Konum izni reddedildi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Toast.makeText(
                        this,
                        "Konum: Lat: $latitude, Lng: $longitude",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this, "Konum alınamadı.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Konum alırken bir hata oluştu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}