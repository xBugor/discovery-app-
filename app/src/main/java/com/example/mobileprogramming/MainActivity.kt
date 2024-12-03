package com.example.mobileprogramming

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.gms.auth.api.signin.GoogleSignInClient


import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        firebase = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val kayit = findViewById<Button>(R.id.kayit)
        kayit.setOnClickListener {
            val intent = Intent(this, KaydolmaEkran::class.java)
            startActivity(intent)
        }
        val giris = findViewById<Button>(R.id.giris)
        val mailgir = findViewById<TextInputLayout>(R.id.mailLayout)
        val sifregir = findViewById<TextInputLayout>(R.id.sifrelayout)

        giris.setOnClickListener {
            val email = mailgir.editText?.text.toString()
            val sifre = sifregir.editText?.text.toString()
            if (email.isNotEmpty() && sifre.isNotEmpty()) {
                firebase.signInWithEmailAndPassword(email,sifre).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Giris Başarılı!",
                            Snackbar.LENGTH_SHORT
                        ).show();

                        val intent = Intent(this, Giris::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            it.exception.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }

            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "İlgili alanları doldurunuz",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}