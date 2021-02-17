package com.jedischool.skinder.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jedischool.skinder.data.api.ApiService
import com.jedischool.skinder.data.repository.MainRepository
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(MainRepository(apiService)
            ) as T
        throw IllegalArgumentException("Unknown Class Name!")
    }
}