package com.example.foodstabook.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R
import com.ms.square.android.expandabletextview.ExpandableTextView

class NewsfeedAdapter(private var mList: List<PostModel>,
private val listener: OnViewCommentsClickListener) : RecyclerView.Adapter<NewsfeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var doubleClickLastTime = 0L
        val post = mList[position]
        var postLikes = "Likes: " + post.postLikes.toString()
        val numComments = "View all " + post.postComments.size.toString() + " comments..."
        var index = 0
        var liked = false

        holder.postLeft.visibility = View.INVISIBLE
        if (post.postImage.size == 1){
            holder.postRight.visibility = View.INVISIBLE
        }
        holder.postAuthor.text = post.postAuthor
        holder.authorIcon.setImageResource(post.authorIcon)
        holder.postImage.setImageResource(post.postImage[0])
        holder.likes.text = postLikes
        holder.postDescription.text = (post.postDescription)
        holder.viewComments.text = numComments

        holder.postLeft.setOnClickListener{
            if(index != 0) {
                index -= 1
                holder.postImage.setImageResource(post.postImage[index])
                holder.postRight.visibility = View.VISIBLE
                if (index == 0)
                    holder.postLeft.visibility = View.INVISIBLE
            }
        }
        holder.postRight.setOnClickListener{
            if(index != post.postImage.size - 1) {
                index += 1
                holder.postImage.setImageResource(post.postImage[index])
                holder.postLeft.visibility = View.VISIBLE
                if (index == post.postImage.size - 1)
                    holder.postRight.visibility = View.INVISIBLE
            }
        }
        holder.postImage.setOnClickListener {
            if ((System.currentTimeMillis() - doubleClickLastTime) < 250) {
                doubleClickLastTime = 0
                if(liked){
                    liked = false
                    post.postLikes -= 1
                    postLikes = "Likes: " + post.postLikes.toString()
                    holder.likes.text = postLikes
                }
                else{
                    liked = true
                    post.postLikes += 1
                    postLikes = "Likes: " + post.postLikes.toString()
                    holder.likes.text = postLikes
                }
            }
            else {
                doubleClickLastTime = System.currentTimeMillis()
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(postView: View) : RecyclerView.ViewHolder(postView), View.OnClickListener {
        var postAuthor: TextView = postView.findViewById(R.id.postAuthor)
        var authorIcon: ImageView = postView.findViewById(R.id.authorIcon)
        var postImage: ImageButton = postView.findViewById(R.id.postImage)
        var likes: TextView = postView.findViewById(R.id.likes)
        var postDescription: ExpandableTextView = postView.findViewById(R.id.postDescription)
        val viewComments: Button = postView.findViewById(R.id.viewCommentsButton)
        val postLeft: ImageButton = postView.findViewById(R.id.postImageLeft)
        val postRight: ImageButton = postView.findViewById(R.id.postImageRight)

        init{
            viewComments.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onViewCommentsClick(position)
        }
    }

    interface OnViewCommentsClickListener{
        fun onViewCommentsClick(position: Int){
        }
    }
}

