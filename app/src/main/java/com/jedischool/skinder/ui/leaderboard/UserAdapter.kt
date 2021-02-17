package com.jedischool.skinder.ui.leaderboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import com.jedischool.skinder.data.model.PostDetail
import com.jedischool.skinder.data.model.UserLeaderboard

class UserAdapter(private val users:ArrayList<UserLeaderboard>, private val context: Context)
    : RecyclerView.Adapter<UserAdapter.DataViewHolder>()
{
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserLeaderboard) {
            val userImage: ImageView = itemView.findViewById(R.id.image_user_card)
            val userName: TextView = itemView.findViewById(R.id.name_user_card)
            val pointsUser: TextView = itemView.findViewById(R.id.points_user_card)
            Glide.with(itemView).load(user.image_link).into(userImage)
            userName.text = user.name
            pointsUser.text = "Points: " + user.points
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