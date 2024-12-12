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
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Embedded
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import com.google.android.material.navigation.NavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Giris : AppCompatActivity() {
    data class EventDetails(
        val name: String,
        val date: String,
        val url: String
    )
    data class EventResponse(
        val _embedded: Embedded?
    )
    data class Embedded(
        val events: List<Event>?
    )
    data class Event(
        val name: String,
        val dates: Dates,
        val url: String
    )
    data class Dates(
        val start: Start
    )
    data class Start(
        val localDate: String
    )
    val eventList = mutableListOf<EventDetails>()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://app.ticketmaster.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(TicketmasterService::class.java)

    interface TicketmasterService {
        @GET("discovery/v2/events.json")
        fun getEvents(
            @Query("apikey") apiKey: String,
            @Query("city") city: String? = null
        ): Call<EventResponse>
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOCATION_PERMISSION_REQUEST_CODE = 1001
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission()) {
            getUserLocation()
        } else {
            requestLocationPermission()
        }

        drawerLayout=findViewById(R.id.main)
        var menubutton=findViewById<ImageButton>(R.id.menu)
        navigationView=findViewById(R.id.cekmece)
        fetchEvents()


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
















       val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEventList)
      recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = EventAdapter(eventList)
      recyclerView.adapter = adapter
    }

    fun fetchEvents() {
        val apiKey = "vmgAK287ultGS5QONMoayZQ0M8iex7Q8"
        val call = service.getEvents(apiKey, city = "Istanbul")

        call.enqueue(object : retrofit2.Callback<EventResponse> {
            override fun onResponse(
                call: retrofit2.Call<EventResponse>,
                response: retrofit2.Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    val events = response.body()?._embedded?.events

                    events?.forEach { event ->
                        val eventDetails = EventDetails(
                            name = event.name,
                            date = event.dates.start.localDate,
                            url = event.url
                        )
                        eventList.add(eventDetails)
                    }

                    // Adapteri yeniden düzenleyip güncelle
                   findViewById<RecyclerView>(R.id.recyclerViewEventList).adapter?.notifyDataSetChanged()

                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<EventResponse>, t: Throwable) {
                println("Failed to fetch events: ${t.message}")
            }
        })
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
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
            }
        }
    }
}
