package com.example.mobileprogramming

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetayActivitiy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var webView: WebView
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detay_activitiy)

        val textViewName = findViewById<TextView>(R.id.eventNameDetail)
        val textViewDate = findViewById<TextView>(R.id.eventDateDetail)
        val textViewVenue = findViewById<TextView>(R.id.eventVenueDetail)
        val textViewAdress = findViewById<TextView>(R.id.eventaddress)
        val buttonOpenUrl = findViewById<Button>(R.id.buybutton)
        val imageView: ImageView = findViewById(R.id.eventDetailImage)






        // Intent'ten gelen verileri alma
        val eventName = intent.getStringExtra("event_name")
        val eventDate = intent.getStringExtra("event_date")
        val eventVenue = intent.getStringExtra("event_venue")
        val eventaddress = intent.getStringExtra("event_address")
        val url = intent.getStringExtra("event_url")
        val eventImage = intent.getStringExtra("eventImage")

        buttonOpenUrl.setOnClickListener {
            val url = url// Açmak istediğiniz URL
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        if (eventImage != null) {
            Picasso.get().load(eventImage).into(imageView)
        }
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