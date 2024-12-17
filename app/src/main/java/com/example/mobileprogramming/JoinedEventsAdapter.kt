
package com.example.mobileprogramming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JoinedEventsAdapter(private val joinedEvents: List<KatildikActivity.JoinedEvent>) :
    RecyclerView.Adapter<JoinedEventsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameText: TextView = itemView.findViewById(R.id.textViewEventName)
        val joinedAtText: TextView = itemView.findViewById(R.id.textViewJoinedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_joined_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val joinedEvent = joinedEvents[position]
        holder.eventNameText.text = "Etkinlik: ${joinedEvent.eventName}"
        holder.joinedAtText.text = "Katılım Tarihi: ${joinedEvent.joinedAt}"
    }

    override fun getItemCount() = joinedEvents.size
}
