package com.jedischool.skinder.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
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
        fun votePost(v: String, id: String)
    }

    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, post: PostDetail, postClicked: PostClicked)
        {
            itemView.apply {
                val postTitle : TextView = findViewById(R.id.name_post_card)
                val postImage : ImageView = findViewById(R.id.image_post_card)
                val postEmail : TextView = findViewById(R.id.desc_post_card)
                val postAuthor : ImageView = findViewById(R.id.author_post_card)
                val postUpVotes : TextView = findViewById(R.id.post_card_upvotes)
                val postDownVotes : TextView = findViewById(R.id.post_card_downvotes)
                val upVote: ImageView = findViewById(R.id.upvote_icon)
                val downVote: ImageView = findViewById(R.id.downvote_icon)
                val comment: ImageView = findViewById(R.id.comment_icon)
                val commentNo: TextView = findViewById(R.id.post_card_comments)
                val username: TextView = findViewById(R.id.post_username)

                username.text = post.name
                postTitle.text = post.title
                Glide.with(itemView).load(post.image_link).into(postImage)
                postEmail.text = post.caption
                Glide.with(itemView).load(post.user_image).into(postAuthor)
                commentNo.text = post.noofcomments.toString()
                postUpVotes.text = post.upvotes.toString()
                postDownVotes.text = post.downvotes.toString()

                comment.setOnClickListener { postClicked.onPostClicked(position) }

                when (post.upordown) {
                    "u" -> {
                        upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                        downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    }
                    "d" -> {
                        downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                        upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    }
                    else -> {
                        downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                        upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    }
                }

                upVote.setOnClickListener {
                    when (post.upordown) {
                        "u" -> {
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            post.upordown=""
                            post.upvotes -= 1
                        }
                        "d" -> {
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            post.downvotes -= 1
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            post.upordown="u"
                            post.upvotes += 1
                        }
                        else -> {
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            post.upordown="u"
                            post.upvotes += 1
                        }
                    }
                    postUpVotes.text = post.upvotes.toString()
                    postDownVotes.text = post.downvotes.toString()
                    itemView.invalidate()
                    postClicked.votePost("u", post.post_id)
                }

                downVote.setOnClickListener {
                    when (post.upordown) {
                        "d" -> {
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            post.upordown=""
                            post.downvotes -= 1
                        }
                        "u" -> {
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            post.upvotes -= 1
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            post.upordown="d"
                            post.downvotes += 1
                        }
                        else -> {
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            post.upordown="d"
                            post.downvotes += 1
                        }
                    }
                    postUpVotes.text = post.upvotes.toString()
                    postDownVotes.text = post.downvotes.toString()
                    itemView.invalidate()
                    postClicked.votePost("d",post.post_id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(context).inflate(R.layout.post_card_item, parent, false))

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(position,posts[position], postClicked)
    }

    fun addPosts(posts: List<PostDetail>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
    }
}