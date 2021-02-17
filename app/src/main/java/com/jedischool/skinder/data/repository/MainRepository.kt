package com.jedischool.skinder.data.repository

import com.jedischool.skinder.data.api.ApiHelper
import okhttp3.MultipartBody

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getToken(t:Map<String,String>) = apiHelper.getToken(t)
    suspend fun refreshToken() = apiHelper.refreshToken()
    suspend fun getPosts() = apiHelper.getPosts()
    suspend fun getMyPosts() = apiHelper.getMyPosts()
    suspend fun getTrending() = apiHelper.getTrending()
    suspend fun getLeaderboard() = apiHelper.getLeaderboard()
    suspend fun getProfile() = apiHelper.getProfile()
    suspend fun addPostImage(img: MultipartBody.Part, t: MultipartBody.Part, c: MultipartBody.Part) = apiHelper.addPostImage(img,t,c)
    suspend fun addPost(t: MultipartBody.Part, c: MultipartBody.Part) = apiHelper.addPost(t,c)
}