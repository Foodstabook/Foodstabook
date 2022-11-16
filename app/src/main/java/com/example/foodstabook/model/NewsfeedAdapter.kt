package com.example.foodstabook.model

import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Color
import android.media.Image
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R

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
        val description = SpannableStringBuilder(post.postDescription)
        val tags = post.postTags
        var index = 0
        var liked = false

        fun Int.toPx() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

        fun View.updateHeight(newHeight: Int) {
            layoutParams = layoutParams.apply {
                height = newHeight
            }
        }

        fun collapseText(){
            val collapsedHeight = holder.collapsedDescription.height
            val heightAnimator = ValueAnimator.ofInt(holder.expandedDescription.height, collapsedHeight)
            heightAnimator.addUpdateListener {
                holder.expandedDescription.updateHeight(it.animatedValue as Int)
            }
            heightAnimator.doOnStart {
                holder.collapseArrow.isVisible = false
            }
            heightAnimator.doOnEnd {
                holder.expandedDescription.isVisible = false
                holder.expandArrow.isVisible = true
                holder.collapsedDescription.isVisible = true
            }
            heightAnimator.start()
        }

        fun expandText() {
            holder.expandedDescription.updateHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT)

                val totalMarginForDescription = 2 * 12.toPx()
            holder.expandedDescription.measure(
                View.MeasureSpec.makeMeasureSpec(
                    holder.clContainer.width - totalMarginForDescription,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.UNSPECIFIED
            )
            val expandedHeight = holder.expandedDescription.measuredHeight

            val heightAnimator = ValueAnimator.ofInt(holder.collapsedDescription.height, expandedHeight)
            heightAnimator.addUpdateListener {
                holder.expandedDescription.height = it.animatedValue as Int
            }

            heightAnimator.doOnStart {
                holder.expandArrow.isVisible = false
                holder.collapsedDescription.isVisible = false
            }
            holder.expandedDescription.isVisible = true
            heightAnimator.doOnEnd {
                holder.collapseArrow.isVisible = true
            }
            heightAnimator.start()
        }

        for (i in tags){
            var tag = " #" + i.filter { !it.isWhitespace() }
            tag = tag.lowercase()
            description.append(tag)
            description.setSpan(ForegroundColorSpan(Color.BLUE), description.indexOf(tag), description.indexOf(tag) + tag.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        holder.postLeft.visibility = View.INVISIBLE
        if (post.postImage.size == 1){
            holder.postRight.visibility = View.INVISIBLE
        }
        holder.expandArrow.setImageResource(R.drawable.purple_down_arrow)
        holder.collapseArrow.setImageResource(R.drawable.purple_up_arrow)
        holder.collapseArrow.visibility = View.GONE
        holder.postAuthor.text = post.postAuthor
        holder.authorIcon.setImageResource(post.authorIcon)
        holder.postImage.setImageResource(post.postImage[0])
        holder.likes.text = postLikes
        holder.collapsedDescription.text = description
        holder.expandedDescription.text = description
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

        holder.collapsedDescription.setOnClickListener{
            expandText()
        }

        holder.expandedDescription.setOnClickListener{
            collapseText()
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
        var collapsedDescription: TextView = postView.findViewById(R.id.collapsedDescription)
        var expandedDescription: TextView = postView.findViewById(R.id.expandedDescription)
        val expandArrow: ImageView = postView.findViewById(R.id.expandArrow)
        val collapseArrow: ImageView = postView.findViewById(R.id.collapseArrow)
        val viewComments: Button = postView.findViewById(R.id.viewCommentsButton)
        val postLeft: ImageButton = postView.findViewById(R.id.postImageLeft)
        val postRight: ImageButton = postView.findViewById(R.id.postImageRight)
        val clContainer: ConstraintLayout = postView.findViewById(R.id.postView)

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
