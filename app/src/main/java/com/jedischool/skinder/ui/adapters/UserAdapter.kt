package com.jedischool.skinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import com.jedischool.skinder.data.model.UserLeaderboard

class UserAdapter(private val users:ArrayList<UserLeaderboard>, private val context: Context)
    : RecyclerView.Adapter<UserAdapter.DataViewHolder>()
{
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserLeaderboard) {
            itemView.apply {
                val userImage: ImageView = findViewById(R.id.image_user_card)
                val userName: TextView = findViewById(R.id.name_user_card)
                val pointsUser: TextView = findViewById(R.id.points_user_card)
                val badge:ImageView = findViewById(R.id.badge_user_card)
                Glide.with(this).load(user.image_link).into(userImage)
                userName.text = user.name
                pointsUser.text = "Points: " + user.points

                when {
                    user.points>80 -> badge.setImageResource(R.drawable.badge5)
                    user.points>60 -> badge.setImageResource(R.drawable.badge4)
                    user.points>40 -> badge.setImageResource(R.drawable.badge3)
                    user.points>20 -> badge.setImageResource(R.drawable.badge2)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(context).inflate(R.layout.user_card_item, parent, false))

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun addPosts(users: ArrayList<UserLeaderboard>) {
        this.users.apply {
            clear()
            addAll(users)
        }
    }
}