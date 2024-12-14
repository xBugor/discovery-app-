package com.example.mobileprogramming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

class FavorilerActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesList: MutableList<EventData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoriler)

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView)
        favoritesList = mutableListOf()
        favoritesAdapter = FavoritesAdapter(favoritesList)

        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        favoritesRecyclerView.adapter = favoritesAdapter

        // Firebase'den favori etkinlikleri çekme
        val database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val favoritesRef = database.reference.child("favorites").child(userId)

        favoritesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoritesList.clear()
                for (dataSnapshot in snapshot.children) {
                    val event = dataSnapshot.getValue(EventData::class.java)
                    if (event != null) {
                        favoritesList.add(event)
                    }
                }
                favoritesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata durumunu işleyebilirsiniz
            }
        })
    }

    data class EventData(
        val eventName: String = "",
        val eventDate: String = "",
        val eventVenue: String = "",
        val eventAddress: String = "",
        val eventImage: String = ""
    )

    // RecyclerView Adapter'ı
    class FavoritesAdapter(private val eventList: List<EventData>) : RecyclerView.Adapter<FavoritesAdapter.EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
            return EventViewHolder(view)
        }

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            val event = eventList[position]
            holder.bind(event)
        }

        override fun getItemCount(): Int = eventList.size

        class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val eventName: TextView = itemView.findViewById(R.id.eventName)
            private val eventDate: TextView = itemView.findViewById(R.id.eventDate)
            private val eventVenue: TextView = itemView.findViewById(R.id.eventVenue)
            private val eventAddress: TextView = itemView.findViewById(R.id.eventAddress)
            private val eventImage: ImageView = itemView.findViewById(R.id.eventImage)

            fun bind(event: EventData) {
                eventName.text = event.eventName
                eventDate.text = event.eventDate
                eventVenue.text = event.eventVenue
                eventAddress.text = event.eventAddress
                Picasso.get().load(event.eventImage).into(eventImage)
            }
        }
    }
}
