package com.example.mobileprogramming

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class KaydolmaEkran : AppCompatActivity() {

    private lateinit var firebase: FirebaseAuth//firebase kontrolü için nesne değişkenin atanması

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_kaydolma_ekran)
        firebase = FirebaseAuth.getInstance()
        val kayitbuton=findViewById<Button>(R.id.kayitolbuton)
        val mailgir=findViewById<TextInputLayout>(R.id.mailLayout)
        val sifregir=findViewById<TextInputLayout>(R.id.sifrelayout)
        val sifrekontrol=findViewById<TextInputLayout>(R.id.sifredogrulalayout)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        kayitbuton.setOnClickListener{
            val email = mailgir.editText?.text.toString()
            val sifre =sifregir.editText?.text.toString()
            val sifreKontrolString=sifrekontrol.editText?.text.toString()
            if (email.isNotEmpty() && sifre.isNotEmpty() && sifreKontrolString.isNotEmpty()) {
                if (sifre == sifreKontrolString) {
                    firebase.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Kayıt Tamamlandı!",
                                Snackbar.LENGTH_SHORT
                            ).show();
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }

                        else {
                            Snackbar.make(findViewById(android.R.id.content), "Kayıt Başarısız: " , Snackbar.LENGTH_LONG)
                                .show();

                        }

                    }


                } else {
                    Toast.makeText(this, "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "İlgili alanları doldurunuz", Toast.LENGTH_SHORT).show()

            }




        }

    }
}