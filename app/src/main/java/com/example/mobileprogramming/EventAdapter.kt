package com.example.mobileprogramming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val eventList: MutableList<Giris.EventDetails>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(event: Giris.EventDetails)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val eventmekan: TextView = itemView.findViewById(R.id.venue)
        val eventTime: TextView = itemView.findViewById(R.id.eventTime)

        fun bind(event: Giris.EventDetails, listener: OnItemClickListener?) {
            itemView.setOnClickListener {
                listener?.onItemClick(event)
                // Tıklanan öğe için arka plan rengini değiştirme
                itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.colorHighlight))  // highlight rengi
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = eventList[position]
        holder.eventName.text = currentEvent.name
        holder.eventDate.text = currentEvent.date
        holder.eventmekan.text = currentEvent.venue
        holder.eventTime.text = "Saat: ${currentEvent.startTime}" // Saat gösterimi

        holder.bind(currentEvent, listener)  // Burada bind fonksiyonunu çağırıyoruz.
    }
    fun updateData(newEventList: List<Giris.EventDetails>) {
        eventList.clear()
        eventList.addAll(newEventList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = eventList.size
}
