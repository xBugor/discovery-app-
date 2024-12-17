package com.example.mobileprogramming

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class KatildikActivity : AppCompatActivity() {

    data class JoinedEvent(
        val eventName: String = "",
        val joinedAt: Long = 0
    )

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JoinedEventsAdapter
    private val joinedEventsList = mutableListOf<JoinedEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katildik)

        recyclerView = findViewById(R.id.recyclerViewJoinedEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JoinedEventsAdapter(joinedEventsList)
        recyclerView.adapter = adapter

        fetchJoinedEvents()
    }

    private fun fetchJoinedEvents() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val participantsRef = FirebaseDatabase.getInstance().reference.child("participants")

        participantsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                joinedEventsList.clear()
                for (eventSnapshot in snapshot.children) {
                    val eventName = eventSnapshot.key ?: continue
                    val userSnapshot = eventSnapshot.child(userId)
                    if (userSnapshot.exists()) {
                        val joinedAt = userSnapshot.child("joinedAt").getValue(Long::class.java) ?: 0
                        joinedEventsList.add(JoinedEvent(eventName, joinedAt))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KatildikActivity, "Veri yüklenemedi!", Toast.LENGTH_SHORT).show()
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }
}
