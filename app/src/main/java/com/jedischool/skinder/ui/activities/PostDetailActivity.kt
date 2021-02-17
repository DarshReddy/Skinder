package com.jedischool.skinder.ui.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jedischool.skinder.R

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val image:ImageView = findViewById(R.id.image_detail_post)
        val title:TextView = findViewById(R.id.title_post_detail)
        val caption:TextView = findViewById(R.id.desc_post_detail)
        val author: ImageView = findViewById(R.id.author_post_detail)


        Glide.with(this).load(intent.getStringExtra("image")).into(image)
        Glide.with(this).load(intent.getStringExtra("author")).into(author)
        title.text = intent.getStringExtra("title")
        caption.text = intent.getStringExtra("caption")
    }
}