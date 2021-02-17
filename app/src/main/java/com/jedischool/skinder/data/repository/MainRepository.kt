package com.jedischool.skinder.data.repository

import com.jedischool.skinder.data.api.ApiService
import okhttp3.MultipartBody

class MainRepository(private val apiService: ApiService) {
    suspend fun getToken(t:Map<String,String>) = apiService.getToken(t)
    suspend fun refreshToken() = apiService.refreshToken()
    suspend fun getPosts() = apiService.getPosts()
    suspend fun getMyPosts() = apiService.getMyPosts()
    suspend fun getTrending() = apiService.getTrending()
    suspend fun getLeaderboard() = apiService.getLeaderboard()
    suspend fun getProfile() = apiService.getProfile()
    suspend fun addPostImage(img: MultipartBody.Part, t: MultipartBody.Part, c: MultipartBody.Part) = apiService.addPostImage(img,t,c)
    suspend fun addPost(t: MultipartBody.Part, c: MultipartBody.Part) = apiService.addPost(t,c)
}