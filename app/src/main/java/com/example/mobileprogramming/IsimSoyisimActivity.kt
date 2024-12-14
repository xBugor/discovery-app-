package com.example.mobileprogramming

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

// IsimSoyisimActivity.kt
class IsimSoyisimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isim_soyisim)

        val isimSoyisimGir = findViewById<TextInputLayout>(R.id.isimSoyisimLayout)
        val devamButon = findViewById<Button>(R.id.devamButon)

        devamButon.setOnClickListener {
            val isimSoyisim = isimSoyisimGir.editText?.text.toString()
            if (isimSoyisim.isNotEmpty()) {
                val email = intent.getStringExtra("email")
                val sifre = intent.getStringExtra("sifre")

                val resultIntent = Intent()
                resultIntent.putExtra("isimSoyisim", isimSoyisim)
                resultIntent.putExtra("email", email)
                resultIntent.putExtra("sifre", sifre)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "İsim ve Soyisim alanını doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
