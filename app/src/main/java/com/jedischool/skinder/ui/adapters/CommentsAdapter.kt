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
import com.jedischool.skinder.data.model.CommentDetail

class CommentsAdapter(private val comments: ArrayList<CommentDetail>, private val context:Context, private val commentClicked: CommentClicked)
    :RecyclerView.Adapter<CommentsAdapter.DataViewHolder>()
{
    interface CommentClicked {
        fun onCommentClicked(pos: Int)
        fun voteComment(v:String, id:String)
    }

    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(comment: CommentDetail, commentClicked: CommentClicked) {
            itemView.apply {
                val userImage: ImageView = findViewById(R.id.image_comment_card)
                val userName: TextView = findViewById(R.id.name_comment_card)
                val commentText: TextView = findViewById(R.id.comment_comment_card)
                val upvotes: TextView = findViewById(R.id.comment_card_upvotes)
                val downvotes: TextView = findViewById(R.id.comment_card_downvotes)
                val upVote:ImageView = findViewById(R.id.comment_upvote_icon)
                val downVote:ImageView = findViewById(R.id.comment_downvote_icon)

                Glide.with(this).load(comment.user_image).into(userImage)
                userName.text = comment.name
                commentText.text = comment.comment
                upvotes.text = comment.upVotes.toString()
                downvotes.text = comment.downVotes.toString()

                when (comment.upordown) {
                    "u" -> {
                        upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                    }
                    "d" -> {
                        downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                    }
                    else -> {
                        downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                        upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    }
                }

                upVote.setOnClickListener {
                    when (comment.upordown) {
                        "u" -> {
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            comment.upordown=""
                            comment.upVotes -= 1
                        }
                        "d" -> {
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            comment.downVotes -= 1
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            comment.upordown="u"
                            comment.upVotes += 1
                        }
                        else -> {
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            comment.upordown="u"
                            comment.upVotes += 1
                        }
                    }
                    upvotes.text = comment.upVotes.toString()
                    downvotes.text = comment.downVotes.toString()
                    itemView.invalidate()
                    commentClicked.voteComment("u", comment.comment_id)
                }

                downVote.setOnClickListener {
                    when (comment.upordown) {
                        "d" -> {
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            comment.upordown=""
                            comment.downVotes -= 1
                        }
                        "u" -> {
                            upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                            comment.upVotes -= 1
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            comment.upordown="d"
                            comment.downVotes += 1
                        }
                        else -> {
                            downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                            comment.upordown="d"
                            comment.downVotes += 1
                        }
                    }
                    upvotes.text = comment.upVotes.toString()
                    downvotes.text = comment.downVotes.toString()
                    itemView.invalidate()
                    commentClicked.voteComment("d",comment.comment_id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(context).inflate(R.layout.comments_card_item, parent, false))

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(comments[position],commentClicked)
        holder.itemView.setOnClickListener {
            commentClicked.onCommentClicked(position)
        }
    }

    fun addPosts(comments: List<CommentDetail>) {
        this.comments.apply {
            clear()
            addAll(comments)
        }
    }
}