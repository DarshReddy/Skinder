package com.jedischool.skinder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jedischool.skinder.data.model.*
import com.jedischool.skinder.data.repository.MainRepository
import com.jedischool.skinder.utils.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getToken(token:Map<String,String>) : LiveData<Resource<AuthResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getToken(token)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun refreshToken() : LiveData<Resource<RefreshTokenResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.refreshToken()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getPosts() : LiveData<Resource<List<PostDetail>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getPosts()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getTrending() : LiveData<Resource<List<PostDetail>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getTrending()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getPopular() : LiveData<Resource<List<PostDetail>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getPopular()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getMyPosts() : LiveData<Resource<List<PostDetail>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getMyPosts()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getLeaderboard() : LiveData<Resource<List<UserLeaderboard>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getLeaderboard()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getProfile() : LiveData<Resource<UserDetailResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getProfile()))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun addPostImage(img:MultipartBody.Part,t:MultipartBody.Part,c: MultipartBody.Part)
            : LiveData<Resource<MessageResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.addPostImage(img,t,c)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun addPost(t:MultipartBody.Part,c: MultipartBody.Part)
            : LiveData<Resource<MessageResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.addPost(t,c)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun votePost(m:Map<String, String>)
            : LiveData<Resource<MessageResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.votePost(m)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun voteComment(m:Map<String, String>)
            : LiveData<Resource<MessageResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.voteComment(m)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun addComment(m:Map<String, String>)
            : LiveData<Resource<MessageResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.addComment(m)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getPostComments(id:String)
            : LiveData<Resource<List<CommentDetail>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getPostComments(id)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }

    fun getThread(id:String)
            : LiveData<Resource<List<CommentDetail>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getThread(id)))
        } catch (e:Exception) {
            emit(Resource.error(data = null, message = e.message ?: "error occurred!"))
        }
    }
}