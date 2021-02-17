package com.jedischool.skinder.data.model

data class PostDetail(
    val post_id : String,
    val user_id : String,
    val title : String,
    val caption : String,
    val image_link : String,
    val upvotes : String,
    val downvotes : String,
    val timeposted : String,
    val upordown : String,
    val name : String,
    val user_image : String
)