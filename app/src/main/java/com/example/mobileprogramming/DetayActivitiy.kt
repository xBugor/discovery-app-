package com.example.mobileprogramming

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetayActivitiy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detay_activitiy)

        val textViewName = findViewById<TextView>(R.id.eventNameDetail)
        val textViewDate = findViewById<TextView>(R.id.eventDateDetail)
        val textViewVenue = findViewById<TextView>(R.id.eventVenueDetail)
        val textViewAdress = findViewById<TextView>(R.id.eventaddress)

        // Intent'ten gelen verileri alın
        val eventName = intent.getStringExtra("event_name")
        val eventDate = intent.getStringExtra("event_date")
        val eventVenue = intent.getStringExtra("event_venue")
        val eventaddress = intent.getStringExtra("event_address")

        // TextView'lere verileri atayın
        textViewName.text = eventName ?: "No Name"
        textViewDate.text = eventDate ?: "No Date"
        textViewVenue.text = eventVenue ?: "No Venue"
        textViewAdress.text = eventaddress ?: "no adress"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}