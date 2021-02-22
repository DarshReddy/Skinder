package com.jedischool.skinder.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import com.jedischool.skinder.data.model.CommentDetail
import com.rey.material.widget.Button

class CommentsAdapter(private val comments: ArrayList<CommentDetail>, private val context: Context, private val commentHelper: CommentHelper)
    :RecyclerView.Adapter<CommentsAdapter.DataViewHolder>()
{
    interface CommentHelper {
        fun loadThread(position: Int, id: String, threadRecycler: ThreadAdapter)
        fun addThread(id: String, comment: String, c_id: String, position: Int, commentId: String, threadAdapter: ThreadAdapter)
        fun voteComment(v:String, id:String)
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val threadRecycler: RecyclerView = itemView.findViewById(R.id.thread_recycler)
        fun bind(comment: CommentDetail, commentHelper: CommentHelper, position: Int, threadAdapter: ThreadAdapter) {
            itemView.apply {
                val userImage: ImageView = findViewById(R.id.image_comment_card)
                val userName: TextView = findViewById(R.id.name_comment_card)
                val commentText: TextView = findViewById(R.id.comment_comment_card)
                val upvotes: TextView = findViewById(R.id.comment_card_upvotes)
                val downvotes: TextView = findViewById(R.id.comment_card_downvotes)
                val upVote:ImageView = findViewById(R.id.comment_upvote_icon)
                val downVote:ImageView = findViewById(R.id.comment_downvote_icon)
                val icon: ImageView = findViewById(R.id.comment_card_icon)
                val replies: TextView = findViewById(R.id.comment_card_replies)
                val threadText: EditText = findViewById(R.id.thread_edit)
                val threadAdd: Button = findViewById(R.id.thread_btn)

                if (comment.noofthreads!=0)
                    replies.text = "View "+comment.noofthreads+" Replies"
                else
                    replies.text = "Reply"
                icon.setOnClickListener {
                    if (threadRecycler.visibility == View.VISIBLE) {
                        if (comment.noofthreads!=0)
                            replies.text = "View "+comment.noofthreads+" Replies"
                        else
                            replies.text = "Reply"
                        threadRecycler.visibility = View.GONE
                        threadAdd.visibility = View.GONE
                        threadText.visibility = View.GONE
                    }
                    else {
                        threadRecycler.visibility = View.VISIBLE
                        threadAdd.visibility = View.VISIBLE
                        threadText.visibility = View.VISIBLE
                        replies.text = "Hide Replies"
                    }
                }

                Glide.with(this).load(comment.user_image).into(userImage)
                userName.text = comment.name
                commentText.text = comment.comment
                upvotes.text = comment.upVotes.toString()
                downvotes.text = comment.downVotes.toString()

                when (comment.upordown) {
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
                    commentHelper.voteComment("u", comment.comment_id)
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
                    commentHelper.voteComment("d",comment.comment_id)
                }

                threadAdd.setOnClickListener {
                    commentHelper.addThread(comment.post_id, threadText.text.toString(), comment.comment_id,position, comment.comment_id, threadAdapter)
                    comment.noofthreads+=1
                    threadText.text.clear()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_item, parent, false))

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        val threadLayoutManager = LinearLayoutManager(
                holder.threadRecycler.context,
                LinearLayoutManager.VERTICAL,
                false
        )

        val threadAdapter = ThreadAdapter(arrayListOf(), context, commentHelper)

        holder.threadRecycler.apply {
            layoutManager = threadLayoutManager
            adapter = threadAdapter
            setRecycledViewPool(viewPool)
        }
        holder.bind(comments[position],commentHelper, position, threadAdapter)
        commentHelper.loadThread(position,comments[position].comment_id,threadAdapter)
    }

    fun addPosts(comments: List<CommentDetail>) {
        this.comments.apply {
            clear()
            addAll(comments)
        }
    }
}