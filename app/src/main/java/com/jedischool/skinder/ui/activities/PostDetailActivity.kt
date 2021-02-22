package com.jedischool.skinder.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.rey.material.widget.Button
import com.rey.material.widget.ProgressView

class PostDetailActivity : AppCompatActivity(), CommentsAdapter.CommentHelper {
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var comments: ArrayList<CommentDetail>
    private lateinit var thread: ArrayList<CommentDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val titleTextView: TextView = findViewById(R.id.title_post_detail)
        val author: ImageView = findViewById(R.id.author_post_detail)
        val desc: TextView = findViewById(R.id.desc_post_detail)
        val commentText: EditText = findViewById(R.id.comment_edit)
        val commentAdd: Button = findViewById(R.id.comment_add_btn)

        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val userImage = intent.getStringExtra("author")
        val caption = intent.getStringExtra("caption")

        Glide.with(this).load(userImage).into(author)
        titleTextView.text = title
        desc.text = caption

        val viewModel =ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        val commentRecycler: RecyclerView = findViewById(R.id.recycler_comments)
        commentRecycler.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentsAdapter(arrayListOf(),this,this)
        commentRecycler.adapter = commentsAdapter
        val progress: ProgressView = findViewById(R.id.progress_comments)

        if (id != null) {
            getComments(id,viewModel,progress,commentRecycler)
        }

        commentAdd.setOnClickListener {
            if(commentText.text.toString() == "") {
                Toast.makeText(this, "Please add comment text!", Toast.LENGTH_LONG).show()
            }
            else {
                val params: MutableMap<String, String> = HashMap()
                params["post_id"] = id!!
                params["comment"] = commentText.text.toString()
                viewModel.addComment(params).observe(this, { inIt ->
                    inIt?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(this, resource.data?.message, Toast.LENGTH_LONG).show()
                                getComments(id, viewModel, progress, commentRecycler)
                                commentText.text.clear()
                            }
                            Status.ERROR -> {
                                Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                                Log.e("ERR", resource.message.toString())
                                if (resource.message.toString().contains("401", ignoreCase = true)) {
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
    }

    private fun getComments(id: String, viewModel: MainViewModel, progress: ProgressView, commentRecycler: RecyclerView) {
        viewModel.getPostComments(id).observe(this, { outIt ->
            outIt?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        comments = (resource.data as ArrayList<CommentDetail>?)!!
                        if(comments.size == 0) Toast.makeText(this, "Be the first one to comment!",Toast.LENGTH_LONG).show()
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

    override fun loadThread(position: Int, id: String, threadRecycler: ThreadAdapter) {
        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        viewModel.getThread(id).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        thread = (resource.data as ArrayList<CommentDetail>?)!!
                        threadRecycler.apply {
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

    override fun addThread(id: String, comment: String, c_id: String, position: Int, commentId: String, threadAdapter: ThreadAdapter) {
        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        if(comment == "") {
            Toast.makeText(this, "Please add reply text!", Toast.LENGTH_LONG).show()
        }
        else {
            val params: MutableMap<String, String> = HashMap()
            params["post_id"] = id
            params["comment"] = comment
            params["up_level_cid"] = c_id
            viewModel.addComment(params).observe(this, { inIt ->
                inIt?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Toast.makeText(this, resource.data?.message, Toast.LENGTH_LONG).show()
                            loadThread(position,commentId,threadAdapter)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                            Log.e("ERR", resource.message.toString())
                            if (resource.message.toString().contains("401", ignoreCase = true)) {
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