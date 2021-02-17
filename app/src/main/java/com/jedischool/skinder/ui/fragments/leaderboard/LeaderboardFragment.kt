    package com.jedischool.skinder.ui.fragments.leaderboard

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
import com.jedischool.skinder.data.model.UserLeaderboard
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status
import com.rey.material.widget.ProgressView

    class LeaderboardFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var users: ArrayList<UserLeaderboard>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userRecycler : RecyclerView = view.findViewById(R.id.recycler_leaderboard)
        userRecycler.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { UserAdapter(arrayListOf(), it) }!!
        userRecycler.adapter = adapter
        val progress : ProgressView = view.findViewById(R.id.progress_leaderboard)

        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)
        viewModel.getLeaderboard().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        users = ((resource.data as ArrayList<UserLeaderboard>?)!!)
                        adapter.apply {
                            addPosts(users)
                            notifyDataSetChanged()
                        }
                        progress.visibility = View.GONE
                        userRecycler.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                        if(resource.message.toString().contains("401",ignoreCase = true)) {

                        }
                    }
                    Status.LOADING -> {
                        progress.visibility = View.VISIBLE
                        userRecycler.visibility = View.GONE
                    }
                }
            }
        })
    }
}