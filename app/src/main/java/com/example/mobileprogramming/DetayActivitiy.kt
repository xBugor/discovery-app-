package com.example.mobileprogramming

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DetayActivitiy : AppCompatActivity() {

    data class EventData(
        val eventName: String,
        val eventDate: String,
        val eventVenue: String,
        val eventAddress: String,
        val eventImage: String
    )

    data class CommentData(
        val username: String = "",
        val userId: String = "", // Varsayılan değer ekliyoruz
        val comment: String = "", // Varsayılan değer ekliyoruz
        val timestamp: Long = 0 // Varsayılan değer ekliyoruz
    )

    data class CommentItemInActivity(
        val username: String,
        val userId: String,
        val comment: String,
        val timestamp: Long
    )

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detay_activitiy)

        val textViewName = findViewById<TextView>(R.id.eventNameDetail)
        val textViewDate = findViewById<TextView>(R.id.eventDateDetail)
        val textViewVenue = findViewById<TextView>(R.id.eventVenueDetail)
        val textViewAdress = findViewById<TextView>(R.id.eventaddress)
        val buttonOpenUrl = findViewById<Button>(R.id.buybutton)
        val favoriler = findViewById<Button>(R.id.favorilerbutonu)
        val submitCommentButton = findViewById<Button>(R.id.btnSubmit)
        val commentInput = findViewById<EditText>(R.id.etComment)
        val commentList = findViewById<RecyclerView>(R.id.commentList)
        val imageView: ImageView = findViewById(R.id.eventDetailImage)

        // Intent'ten gelen verileri alma
        val eventName = intent.getStringExtra("event_name")
        val eventDate = intent.getStringExtra("event_date")
        val eventVenue = intent.getStringExtra("event_venue")
        val eventaddress = intent.getStringExtra("event_address")
        val url = intent.getStringExtra("event_url")
        val eventImage = intent.getStringExtra("eventImage")

        buttonOpenUrl.setOnClickListener {
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
        textViewAdress.text = eventaddress ?: "No Address"

        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.reference.child("favorites")
        val commentsRef = database.reference.child("comments").child(eventName ?: "UnknownEvent")

        favoriler.setOnClickListener {
            val eventData = EventData(
                eventName ?: "No Name",
                eventDate ?: "No Date",
                eventVenue ?: "No Venue",
                eventaddress ?: "No Address",
                eventImage ?: ""
            )
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val username = FirebaseAuth.getInstance().currentUser?.displayName ?: return@setOnClickListener
            val userFavoritesRef = favoritesRef.child(userId)
            userFavoritesRef.push().setValue(eventData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Etkinlik favorilere eklendi!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Favorilere ekleme başarısız!", Toast.LENGTH_SHORT).show()
                }
        }
        // Yorum gönderme
        submitCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString().trim()
            if (commentText.isNotEmpty()) {
                println("dene")
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
                var username: String = FirebaseAuth.getInstance().currentUser?.displayName ?: ""

                if (username.isEmpty()) {
                    val database = FirebaseDatabase.getInstance()
                    println("dene1")

                    val userRef = database.getReference("Users").child(userId)//bu hatayı bulmak için 4 saat uğraştım

                    userRef.get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                // Kullanıcının isimSoyisim bilgisini alıyoruz
                                username = snapshot.child("isimSoyisim").getValue(String::class.java) ?: "Kullanıcı Adı" // Varsayılan değer olarak "Kullanıcı Adı" eklenebilir
                                println(username)

                                // Yorum verisini gönder
                                val commentData = CommentData(
                                    username,
                                    userId,
                                    commentText,
                                    System.currentTimeMillis()
                                )
                                commentsRef.push().setValue(commentData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Yorum gönderildi!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        commentInput.text.clear()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Yorum gönderilemedi!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("RealtimeDB", "Hata oluştu: ", e)
                        }
                }
                else{
                    val commentData = CommentData(username, userId, commentText, System.currentTimeMillis())
                    commentsRef.push().setValue(commentData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Yorum gönderildi!", Toast.LENGTH_SHORT).show()
                            commentInput.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Yorum gönderilemedi!", Toast.LENGTH_SHORT).show()
                        }
                }




            } else {
                Toast.makeText(this, "Yorum boş olamaz!", Toast.LENGTH_SHORT).show()
            }
        }

        // Yorumları listeleme
        val commentsAdapter = CommentsAdapter(mutableListOf())
        commentList.layoutManager = LinearLayoutManager(this)
        commentList.adapter = commentsAdapter

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = mutableListOf<CommentData>()
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(CommentData::class.java)
                    if (comment != null) {
                        comments.add(comment)
                    }
                }

                // CommentData'yı CommentItem'a dönüştür
                val commentItems = comments.map { it.toCommentItem() }
                commentsAdapter.updateComments(commentItems)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetayActivitiy, "Yorumlar yüklenemedi!", Toast.LENGTH_SHORT).show()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // CommentData'dan CommentItem'a dönüşüm fonksiyonu
    fun CommentData.toCommentItem(): CommentItem {
        return CommentItem(
            username=this.username,
            userId = this.userId,
            comment = this.comment,
            timestamp = this.timestamp
        )
    }
}
