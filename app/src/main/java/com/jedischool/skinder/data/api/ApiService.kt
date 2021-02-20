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

    @GET("posts/trending/")
    suspend fun getTrending(): List<PostDetail>

    @GET("posts/popular/")
    suspend fun getPopular(): List<PostDetail>

    @GET("users/leaderboard/")
    suspend fun getLeaderboard(): List<UserLeaderboard>

    @GET("users/")
    suspend fun getProfile(): UserDetailResponse

    @Multipart
    @POST("posts/")
    suspend fun addPostImage(@Part image: MultipartBody.Part, @Part title: MultipartBody.Part,
                        @Part caption: MultipartBody.Part): MessageResponse

    @Multipart
    @POST("posts/")
    suspend fun addPost(@Part title: MultipartBody.Part, @Part caption: MultipartBody.Part): MessageResponse

    @PUT("posts/uord/")
    @FormUrlEncoded
    suspend fun votePost(@FieldMap params: Map<String, String>): MessageResponse

    @GET("{post_id}/comments/")
    suspend fun getPostComments(@Path("post_id") id:String): List<CommentDetail>

    @PUT("comments/uord/")
    @FormUrlEncoded
    suspend fun voteComment(@FieldMap params: Map<String, String>): MessageResponse

    @GET("comments/{comment_id}/thread")
    suspend fun getThread(@Path("comment_id") id: String): List<CommentDetail>
}