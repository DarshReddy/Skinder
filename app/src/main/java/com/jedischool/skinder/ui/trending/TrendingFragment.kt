package com.jedischool.skinder.ui.trending

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jedischool.skinder.R
import com.jedischool.skinder.data.api.ApiHelper
import com.jedischool.skinder.data.api.RetrofitBuilder
import com.jedischool.skinder.data.model.PostDetail
import com.jedischool.skinder.ui.activities.LoginActivity
import com.jedischool.skinder.ui.activities.PostDetailActivity
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.home.PostAdapter
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status
import com.rey.material.widget.ProgressView

class TrendingFragment : Fragment(), PostAdapter.PostClicked {

    private lateinit var postAdapter: PostAdapter
    private lateinit var posts: ArrayList<PostDetail>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trendingRecycler : RecyclerView = view.findViewById(R.id.recycler_trending)
        trendingRecycler.layoutManager = LinearLayoutManager(context)
        postAdapter = context?.let {  PostAdapter(arrayListOf(), it, this) }!!
        trendingRecycler.adapter = postAdapter
        val progressView: ProgressView = view.findViewById(R.id.progress_trending)
        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
        viewModel.getPosts().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        posts = (resource.data as ArrayList<PostDetail>?)!!
                        postAdapter.apply {
                            addPosts(posts)
                            notifyDataSetChanged()
                        }
                        progressView.visibility = View.GONE
                        trendingRecycler.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {
                            LoginActivity.refresh()
                        }
                    }
                    Status.LOADING -> {
                        progressView.visibility = View.VISIBLE
                        trendingRecycler.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onPostClicked(pos: Int) {
        val intent = Intent(context, PostDetailActivity::class.java)
        intent.putExtra("title",posts[pos].title)
        intent.putExtra("image",posts[pos].image_link)
        intent.putExtra("caption",posts[pos].caption)
        intent.putExtra("author",posts[pos].user_image)
        startActivity(intent)
    }
}