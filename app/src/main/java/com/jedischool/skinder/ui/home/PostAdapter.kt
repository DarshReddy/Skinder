package com.jedischool.skinder.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import com.jedischool.skinder.data.model.PostDetail

class PostAdapter(private val posts: ArrayList<PostDetail>, private val context: Context, private val postClicked: PostClicked)
    : RecyclerView.Adapter<PostAdapter.DataViewHolder>()
{

    interface PostClicked {
        fun onPostClicked(pos: Int)
    }

    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(post : PostDetail)
        {
            itemView.apply {
                val postTitle : TextView = findViewById(R.id.name_post_card)
                val postImage : ImageView = findViewById(R.id.image_post_card)
                val postEmail : TextView = findViewById(R.id.desc_post_card)
                val postAuthor : ImageView = findViewById(R.id.author_post_card)
                val postUpVotes : TextView = findViewById(R.id.post_card_upvotes)
                val postDownVotes : TextView = findViewById(R.id.post_card_downvotes)
                postTitle.text = post.title
                Glide.with(itemView).load(post.image_link).into(postImage)
                postEmail.text = post.caption
                Glide.with(itemView).load(post.user_image).into(postAuthor)
                postUpVotes.text = post.upvotes
                postDownVotes.text = post.downvotes
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(context).inflate(R.layout.post_card_item, parent, false))

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(posts[position])
        holder.itemView.setOnClickListener {
            postClicked.onPostClicked(position)
        }
    }

    fun addPosts(posts: List<PostDetail>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
    }
}