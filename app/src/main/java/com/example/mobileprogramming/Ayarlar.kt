package com.example.mobileprogramming

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// User sınıfını oluşturuyoruz
data class User(
    val email: String = "",
    val isimSoyisim: String = ""
)

class Ayarlar : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ayarlar)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val emailEditText = findViewById<EditText>(R.id.email)

        // Mevcut kullanıcı ID'sini al
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            println("kullanıcı id")
            println(currentUser.uid)

            // Firebase'den kullanıcı bilgilerini al
            getUserInfo(userId, usernameEditText, emailEditText)


            // Değişiklikleri kaydetme işlemi
            val saveButton = findViewById<Button>(R.id.save_button)
            saveButton.setOnClickListener {
                val newUsername = usernameEditText.text.toString()
                val newEmail = emailEditText.text.toString()

                // Firebase'e yeni bilgileri kaydet
                updateUserInfo(userId, newEmail, newUsername)
            }
        } else {
            Toast.makeText(this, "Kullanıcı oturum açmamış", Toast.LENGTH_SHORT).show()
        }
    }

    // Kullanıcı bilgilerini Firebase'den çekme
    fun getUserInfo(userId: String, usernameEditText: EditText, emailEditText: EditText) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
        usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    usernameEditText.setText(it.isimSoyisim)
                    emailEditText.setText(it.email)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Ayarlar, "Hata: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Kullanıcı bilgilerini Firebase'e güncelleme
    fun updateUserInfo(userId: String, newEmail: String, newIsimSoyisim: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        val updates = hashMapOf<String, Any>(
            "email" to newEmail,
            "isimSoyisim" to newIsimSoyisim
        )
        userRef.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Kullanıcı bilgileri güncellendi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Hata: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
