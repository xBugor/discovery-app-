package com.example.mobileprogramming

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CommentItem(val userId: String,val username: String ,val comment: String, val timestamp: Long)

class CommentsAdapter(private val comments: MutableList<CommentItem>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView = itemView.findViewById(R.id.commentUser)
        val commentTextView: TextView = itemView.findViewById(R.id.commentText)
        val timestampTextView: TextView = itemView.findViewById(R.id.commentTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        println("abunun içinde")
        println(comment.username)
        holder.commentTextView.setTextColor(Color.BLACK);

        holder.userTextView.text = comment.username // Firebase'den kullanıcı adı alınabilir
        holder.userTextView.setTextColor(Color.BLACK);

        holder.commentTextView.text = comment.comment
        holder.timestampTextView.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
            Date(comment.timestamp)
        )
        holder.timestampTextView.setTextColor(Color.BLACK);

    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun updateComments(newComments: List<CommentItem>) {
        comments.clear()
        comments.addAll(newComments)
        notifyDataSetChanged()
    }
}
