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
import android.text.Highlights
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Embedded
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.ktor.http.ContentType// bu
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Giris : AppCompatActivity(),EventAdapter.OnItemClickListener {



    var kullaniciadi="bb"
    data class EventDetails(
        val id: String,             // Etkinlik ID'si
        val name: String,
        val date: String,
        val url: String,
        val venue: String,  // Mekan bilgisi
        val address: String,
        val imageUrl: String?      // Etkinlik resmi

    )
    data class Address(
        val line1: String?, // Adresin ilk satırı
        val line2: String?, // Adresin ikinci satırı (opsiyonel)
        val postalCode: String? // Posta kodu (opsiyonel)
    )
    data class EventResponse(
        val _embedded: Embedded?
    )
    data class Embedded(
        val events: List<Event>?
    )
    data class Event(
        val id: String,               // Etkinlik ID'si

        val name: String,
        val dates: Dates,
        val url: String,
        val _embedded: EventEmbedded?,
        val images: List<Image>?      // Resim bilgisi bide bu


    )
    data class Image(
        val url: String               // Etkinlik resminin URL'si
    )
    data class EventEmbedded(
        val venues: List<Venue>?
    )
    data class Venue(
        val name: String, // Mekan adı
        val address: Address?, // Adres bilgisi
        val city: City? // Şehir bilgisi
    )
    data class City(
        val name: String? // Şehir adı
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
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        var googledangelenemail = intent.getStringExtra("email")
         val googledangelenemaad = intent.getStringExtra("name")


        //   println(googledangelenemaad)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission()) {
            getUserLocation()
        } else {
            requestLocationPermission()
        }
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(userId.toString())
        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Veri başarıyla alındı
                val dataSnapshot = task.result
                val isimSoyisim = dataSnapshot?.child("isimSoyisim")?.value as? String
                val email = dataSnapshot?.child("email")?.value as? String

             var ad=findViewById<TextView>(R.id.adsoyad)
                var eposta=findViewById<TextView>(R.id.epostacek)
                if(googledangelenemaad!=null){
                    ad.text=googledangelenemaad
                    eposta.text=googledangelenemail
                }
                else {
                    ad.text = isimSoyisim
                    eposta.text = email
                }

                // Veriyi kullanabilirsiniz
                Log.d("Firebase", "İsim Soyisim: $isimSoyisim, Email: $email")
            } else {
                // Hata durumunda yapılacak işlemler
                Log.e("Firebase", "Veri çekilemedi: ${task.exception?.message}")
            }
        }

        drawerLayout=findViewById(R.id.main)
        var menubutton=findViewById<ImageButton>(R.id.menu)
        navigationView=findViewById(R.id.cekmece)
        fetchEvents()
        println("All events: $eventList")


        menubutton.setOnClickListener(){
            drawerLayout.open()
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {


                R.id.Harita ->{
                    val intent = Intent(this, Harita::class.java)
                    startActivity(intent)
                }
                R.id.Favoriler ->{
                    val intent = Intent(this, FavorilerActivity::class.java)
                    startActivity(intent)
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
        lateinit var eventAdapter: EventAdapter

       val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEventList)
      recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = EventAdapter(eventList)

      recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
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
                        // Mekan bilgilerini al
                        val venue = event._embedded?.venues?.firstOrNull()
                        val venueName = venue?.name ?: "Unknown Venue"
                        val venueAddress = venue?.address?.line1 ?: "Address not available"
                        val cityName = venue?.city?.name ?: "City not available"
                        val imageUrl = event.images?.firstOrNull()?.url


                        // Adres bilgisini düzenle
                        val fullAddress = if (venueAddress != "Address not available") {
                            "$venueAddress, $cityName"
                        } else {
                            cityName
                        }

                        // Etkinlik bilgilerini listeye ekle
                        val eventDetails = EventDetails(
                            id = event.id,
                            name = event.name,
                            date = event.dates.start.localDate,
                            venue = venueName,
                            address = fullAddress,
                            url = event.url,
                            imageUrl = imageUrl

                        )
                        eventList.add(eventDetails)


                    }
                    // RecyclerView adaptörünü güncelle
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

    override fun onItemClick(event: Giris.EventDetails) {
        // Tıklanan öğenin detaylarını DetailActivity'ye gönder
        val intent = Intent(this, DetayActivitiy::class.java)
        intent.putExtra("event_name", event.name)
        intent.putExtra("event_date", event.date)
        intent.putExtra("event_venue", event.venue)
        intent.putExtra("event_address", event.address)
        intent.putExtra("event_url",event.url)
        intent.putExtra("eventImage",event.imageUrl)

        startActivity(intent)
    }
}
