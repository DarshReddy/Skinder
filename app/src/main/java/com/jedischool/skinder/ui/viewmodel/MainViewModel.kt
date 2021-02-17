package com.jedischool.skinder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jedischool.skinder.data.model.*
import com.jedischool.skinder.data.repository.MainRepository
import com.jedischool.skinder.utils.Resource
import okhttp3.MultipartBody
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getToken(token:Map<String,String>) : LiveData<Resource<AuthResponse>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getToken(token)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun refreshToken() : LiveData<Resource<RefreshTokenResponse>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.refreshToken()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getPosts() : LiveData<Resource<List<PostDetail>>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getPosts()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getTrending() : LiveData<Resource<List<PostDetail>>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getTrending()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getMyPosts() : LiveData<Resource<List<PostDetail>>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getMyPosts()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getLeaderboard() : LiveData<Resource<List<UserLeaderboard>>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getLeaderboard()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getProfile() : LiveData<Resource<UserDetailResponse>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getProfile()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun addPostImage(img:MultipartBody.Part,t:MultipartBody.Part,c: MultipartBody.Part)
            : LiveData<Resource<AddPostResponse>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.addPostImage(img,t,c)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun addPost(t:MultipartBody.Part,c: MultipartBody.Part)
            : LiveData<Resource<AddPostResponse>> = liveData {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.addPost(t,c)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

}