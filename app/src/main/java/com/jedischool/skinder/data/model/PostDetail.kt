package com.jedischool.skinder.data.model

data class PostDetail(
        val post_id : String,
        val user_id : String,
        val title : String,
        val caption : String,
        val image_link : String,
        var upvotes : Int,
        var downvotes : Int,
        var total : Int,
        val noofcomments: Int,
        val timeposted : String,
        var upordown : String,
        val name : String,
        val user_image : String
)