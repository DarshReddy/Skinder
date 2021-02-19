package com.jedischool.skinder.data.model

data class CommentDetail(
        val comment_id : String,
        val post_id : String,
        val User_id : String,
        val comment : String,
        val up_level_cid : String,
        var upVotes : Int,
        var downVotes : Int,
        val timeCommented : String,
        var upordown : String,
        val name : String,
        val user_image : String,
)