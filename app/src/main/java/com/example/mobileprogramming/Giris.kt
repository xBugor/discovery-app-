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
    val eventList = mutableListOf<EventDetails>()

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
                        // Her bir etkinliği EventDetails nesnesi olarak listeye ekliyoruz
                        val eventDetails = EventDetails(
                            name = event.name,
                            date = event.dates.start.localDate,
                            url = event.url
                        )
                        eventList.add(eventDetails)  // Listeye ekleme

                        // Bilgileri ekrana yazdırıyoruz (isteğe bağlı)
                        println("Event Name: ${eventDetails.name}")
                        println("Event Date: ${eventDetails.date}")
                        println("Event URL: ${eventDetails.url}")
                    }
                    println("Bidaha")
                    println("All events: $eventList")
                    println("deiyom")
                    println(eventList[0])



                }

                else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<EventResponse>, t: Throwable) {
                println("Failed to fetch events: ${t.message}")
            }
        })
    }


    val retrofit = Retrofit.Builder()
        .baseUrl("https://app.ticketmaster.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(TicketmasterService::class.java)


    interface TicketmasterService {
        @GET("discovery/v2/events.json")
        fun getEvents(
            @Query("apikey") apiKey: String,
            @Query("keyword") keyword: String? = null,
            @Query("city") city: String? = null
        ): Call<EventResponse>
    }

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
        fetchEvents()




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