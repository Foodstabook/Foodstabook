package com.example.foodstabook.model

import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlinx.android.synthetic.main.comment_card.view.*

class CommentsAdapter(private val mList: List<CommentsModel>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_card, parent, false)

        return CommentsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {
        val comment = mList[position]

        holder.commentBody.text = comment.commentAuthor + " " + comment.commentBody
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(commentsView: View) : RecyclerView.ViewHolder(commentsView) {
        val authorIcon: ImageView = commentsView.findViewById(R.id.authorIcon)
        val commentBody: TextView = commentsView.findViewById(R.id.commentBody)
    }
}