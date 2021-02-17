package com.jedischool.skinder.data.model

data class UserDetailResponse(
        val User_Id : String,
        val Name : String,
        val Image_Link : String,
        val Points : Int,
        val Email: String,
        val joinedOn : String
)
