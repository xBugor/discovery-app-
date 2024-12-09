package com.example.mobileprogramming

import android.content.Intent
import android.Manifest

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
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject

class Giris : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
         lateinit var drawerLayout: DrawerLayout
         lateinit var navigationView: NavigationView
         lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        }







        fun getLocationAndFetchEvents() {

    }








}







}