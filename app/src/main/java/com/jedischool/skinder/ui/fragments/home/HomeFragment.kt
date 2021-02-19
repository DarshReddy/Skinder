package com.jedischool.skinder.ui.fragments.home

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
import com.jedischool.skinder.data.api.RetrofitBuilder
import com.jedischool.skinder.data.model.PostDetail
import com.jedischool.skinder.databinding.FragmentHomeBinding
import com.jedischool.skinder.ui.activities.LoginActivity
import com.jedischool.skinder.ui.activities.PostDetailActivity
import com.jedischool.skinder.ui.adapters.PostAdapter
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status
import com.rey.material.widget.ProgressView

class HomeFragment : Fragment(), PostAdapter.PostClicked {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var posts: ArrayList<PostDetail>
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postRecycler : RecyclerView = view.findViewById(R.id.recycler_home)
        postRecycler.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { PostAdapter(arrayListOf(), it, this) }!!
        postRecycler.adapter = adapter
        val progress : ProgressView = view.findViewById(R.id.progress_home)

        val viewModel =ViewModelProvider(
            this,
            ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)
        viewModel.getPosts().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        posts = (resource.data as ArrayList<PostDetail>?)!!
                        adapter.apply {
                            addPosts(posts)
                            notifyDataSetChanged()
                        }
                        progress.visibility = View.GONE
                        postRecycler.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Status.LOADING -> {
                        progress.visibility = View.VISIBLE
                        postRecycler.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onPostClicked(pos: Int) {
        val intent = Intent(context, PostDetailActivity::class.java)
        intent.putExtra("id",posts[pos].post_id)
        intent.putExtra("title",posts[pos].title)
        intent.putExtra("image",posts[pos].image_link)
        intent.putExtra("caption",posts[pos].caption)
        intent.putExtra("author",posts[pos].user_image)
        intent.putExtra("up",posts[pos].upvotes)
        intent.putExtra("down",posts[pos].downvotes)
        intent.putExtra("uord",posts[pos].upordown)
        startActivity(intent)
    }

    override fun votePost(v: String, id: String) {
        val viewModel =ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        val params: MutableMap<String, String> = HashMap()
        params["post_id"] = id
        params["upordown"] = v

        viewModel.votePost(params).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(context,"Voted!",Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {
                            val intent = Intent(context, LoginActivity::class.java)
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