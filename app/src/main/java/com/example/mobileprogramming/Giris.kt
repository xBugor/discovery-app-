package com.example.mobileprogramming

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout

class Giris : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
         lateinit var drawerLayout: DrawerLayout



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

        menubutton.setOnClickListener(){
            drawerLayout.open()
        }


    }


}