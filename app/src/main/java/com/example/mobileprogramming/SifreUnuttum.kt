package com.example.mobileprogramming

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SifreUnuttum : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sifre_unuttum)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sifreUnuttum = findViewById<Button>(R.id.sifregonder)
        val mailgir = findViewById<TextInputLayout>(R.id.mailLayout)
        val auth = FirebaseAuth.getInstance()
        val girisgit=findViewById<Button>(R.id.girisgit)


        girisgit.setOnClickListener(){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



        sifreUnuttum.setOnClickListener() {
            val email = mailgir.editText?.text.toString()

            if (email.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "İlgili alanları doldurunuz",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Şifre sıfırlama e-postası gönderildi",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Bir hata oluştu: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }


    }}