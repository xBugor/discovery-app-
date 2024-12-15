package com.example.mobileprogramming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CommentItem(val userId: String, val comment: String, val timestamp: Long)

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


        holder.userTextView.text = comment.userId // Firebase'den kullanıcı adı alınabilir
        holder.commentTextView.text = comment.comment
        holder.timestampTextView.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
            Date(comment.timestamp)
        )
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
