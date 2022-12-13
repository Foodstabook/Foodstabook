package com.example.foodstabook.model

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R
import java.lang.reflect.Type


class CommentsAdapter(private val mList: List<CommentsModel>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = mList[position]
        val commentBody = SpannableStringBuilder(comment.commentAuthor + " " + comment.commentBody)
        commentBody.setSpan(StyleSpan(Typeface.BOLD), 0, comment.commentAuthor.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        holder.commentBody.text = commentBody
        holder.authorIcon.setImageURI(comment.authorIcon.toUri())
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(commentsView: View) : RecyclerView.ViewHolder(commentsView) {
        val authorIcon: ImageView = commentsView.findViewById(R.id.authorIcon)
        val commentBody: TextView = commentsView.findViewById(R.id.commentBody)
    }
}