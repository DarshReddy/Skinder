package com.jedischool.skinder.data.model

data class AuthResponse(
        val accessToken : String,
        val image_link : String,
        val name : String,
        val points : Int
)