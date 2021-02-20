package com.jedischool.skinder.ui.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import com.jedischool.skinder.data.api.RetrofitBuilder
import com.jedischool.skinder.data.model.CommentDetail
import com.jedischool.skinder.ui.adapters.CommentsAdapter
import com.jedischool.skinder.ui.adapters.ThreadAdapter
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status
import com.rey.material.widget.ProgressView

class PostDetailActivity : AppCompatActivity(), CommentsAdapter.CommentHelper {
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var comments: ArrayList<CommentDetail>
    private lateinit var thread: ArrayList<CommentDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val image:ImageView = findViewById(R.id.image_detail_post)
        val titleTextView: TextView = findViewById(R.id.title_post_detail)
        val captionTextView: TextView = findViewById(R.id.desc_post_detail)
        val author: ImageView = findViewById(R.id.author_post_detail)
        val upVote: ImageView = findViewById(R.id.upvote_detail_icon)
        val downVote: ImageView = findViewById(R.id.downvote_detail_icon)
        val postUpVotes : TextView = findViewById(R.id.post_detail_upvotes)
        val postDownVotes : TextView = findViewById(R.id.post_detail_downvotes)

        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val caption = intent.getStringExtra("caption")
        val imageUrl = intent.getStringExtra("image")
        var upvotes = intent.getIntExtra("up",0)
        var downvotes = intent.getIntExtra("down",0)
        var upordown = intent.getStringExtra("uord")
        val userImage = intent.getStringExtra("author")

        postUpVotes.text = upvotes.toString()
        postDownVotes.text = downvotes.toString()

        when (upordown) {
            "u" -> {
                upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            }
            "d" -> {
                downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            }
            else -> {
                upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
            }
        }

        upVote.setOnClickListener {
            when (upordown) {
                "u" -> {
                    upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    upordown=""
                    upvotes -= 1
                }
                "d" -> {
                    downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    downvotes -= 1
                    upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                    upordown="u"
                    upvotes += 1
                }
                else -> {
                    upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                    upordown="u"
                    upvotes += 1
                }
            }
            postUpVotes.text = upvotes.toString()
            postDownVotes.text = downvotes.toString()
            votePost("u", id!!)
        }

        downVote.setOnClickListener {
            when (upordown) {
                "d" -> {
                    downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    upordown=""
                    downvotes -= 1
                }
                "u" -> {
                    upVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    upvotes -= 1
                    downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                    upordown="d"
                    downvotes += 1
                }
                else -> {
                    downVote.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                    upordown="d"
                    downvotes += 1
                }
            }
            postUpVotes.text = upvotes.toString()
            postDownVotes.text = downvotes.toString()
            votePost("d",id!!)
        }


        Glide.with(this).load(imageUrl).into(image)
        Glide.with(this).load(userImage).into(author)
        titleTextView.text = title
        captionTextView.text = caption

        val viewModel =ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        val commentRecycler: RecyclerView = findViewById(R.id.recycler_comments)
        commentRecycler.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentsAdapter(arrayListOf(),this,this,viewModel)
        commentRecycler.adapter = commentsAdapter
        val progress: ProgressView = findViewById(R.id.progress_comments)

        if (id != null) {
            viewModel.getPostComments(id).observe(this, { outIt ->
                outIt?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            comments = (resource.data as ArrayList<CommentDetail>?)!!
                            commentsAdapter.apply {
                                addPosts(comments)
                                notifyDataSetChanged()
                            }
                            progress.visibility = View.GONE
                            commentRecycler.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                            Log.e("ERR", resource.message.toString())
                            if(resource.message.toString().contains("401",ignoreCase = true)) {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        Status.LOADING -> {
                            progress.visibility = View.VISIBLE
                            commentRecycler.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }

    private fun votePost(v: String, id: String) {
        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        val params: MutableMap<String, String> = HashMap()
        params["post_id"] = id
        params["upordown"] = v

        viewModel.votePost(params).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this,"Voted!", Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    override fun loadThread(id: String, threadRecycler: ThreadAdapter) {
        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        viewModel.getThread(id).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        thread = (resource.data as ArrayList<CommentDetail>?)!!
                        threadRecycler?.apply {
                            addComments(thread)
                            notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    override fun voteComment(v: String, id: String) {
        val viewModel =ViewModelProvider(
            this,
            ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        val params: MutableMap<String, String> = HashMap()
        params["comment_id"] = id
        params["upordown"] = v

        viewModel.voteComment(params).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this,"Voted!",Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }
}