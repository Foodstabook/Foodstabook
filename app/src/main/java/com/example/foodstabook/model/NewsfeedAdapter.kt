package com.example.foodstabook.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R

class NewsfeedAdapter(private val mList: List<PostModel>) : RecyclerView.Adapter<NewsfeedAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = mList[position]
        val postLikes = "Likes: " + post.postLikes.toString()
        val numComments = "View all " + post.postComments.size.toString() + " comments..."

        holder.postAuthor.text = post.postAuthor
        holder.postImage.setImageResource(post.postImage)
        holder.likes.text = postLikes
        holder.postDescription.text = post.postDescription
        holder.viewComments.text = numComments

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(postView: View) : RecyclerView.ViewHolder(postView) {
        val postAuthor: TextView = postView.findViewById(R.id.postAuthor)
        val postImage: ImageView = postView.findViewById(R.id.postImage)
        val likes: TextView = postView.findViewById(R.id.likes)
        val postDescription: TextView = postView.findViewById(R.id.postDescription)
        val viewComments: TextView = postView.findViewById(R.id.viewCommentsButton)
    }
}