package com.jedischool.skinder.data.api

import com.jedischool.skinder.data.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("auth/api/oauth/")
    @FormUrlEncoded
    suspend fun getToken(@FieldMap params: Map<String,String>): AuthResponse

    @POST("auth/api/refresh/")
    suspend fun refreshToken(): RefreshTokenResponse

    @GET("posts/")
    suspend fun getPosts(): List<PostDetail>

    @GET("posts/me/")
    suspend fun getMyPosts(): List<PostDetail>

    @GET("posts/popular/")
    suspend fun getTrending(): List<PostDetail>

    @GET("users/leaderboard/")
    suspend fun getLeaderboard(): List<UserLeaderboard>

    @GET("users/")
    suspend fun getProfile(): UserDetailResponse

    @Multipart
    @POST("posts/")
    suspend fun addPostImage(@Part image: MultipartBody.Part, @Part title: MultipartBody.Part,
                        @Part caption: MultipartBody.Part): AddPostResponse

    @Multipart
    @POST("posts/")
    suspend fun addPost(@Part title: MultipartBody.Part, @Part caption: MultipartBody.Part): AddPostResponse
}