package com.jedischool.skinder.ui.fragments.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import com.jedischool.skinder.data.api.RetrofitBuilder
import com.jedischool.skinder.data.model.UserDetailResponse
import com.jedischool.skinder.ui.activities.LoginActivity
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status

class ProfileFragment : Fragment() {

    private lateinit var user:UserDetailResponse
    private lateinit var badge2:ImageView
    private lateinit var badge3:ImageView
    private lateinit var badge4:ImageView
    private lateinit var badge5:ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image:ImageView = view.findViewById(R.id.profile_image)
        val username:TextView = view.findViewById(R.id.profile_username)
        val userEmail: TextView = view.findViewById(R.id.profile_user_email)
        val points:TextView = view.findViewById(R.id.profile_user_points)
        badge2 = view.findViewById(R.id.profile_badge_2)
        badge3 = view.findViewById(R.id.profile_badge_3)
        badge4 = view.findViewById(R.id.profile_badge_4)
        badge5 = view.findViewById(R.id.profile_badge_5)

        val viewModel =ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)
        viewModel.getProfile().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        user = resource.data!!
                        Glide.with(view).load(user.Image_Link).into(image)
                        username.text = user.Name
                        userEmail.text = user.Email
                        points.text = "Points: " + user.Points
                        setBadges(user.Points)
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

    private fun setBadges(points: Int) {
        if(points>40) badge5.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        if(points>30) badge4.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        if(points>20) badge3.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        if(points>10) badge2.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
    }
}