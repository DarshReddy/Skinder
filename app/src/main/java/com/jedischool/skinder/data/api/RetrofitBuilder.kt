package com.jedischool.skinder.data.api

import com.jedischool.skinder.ui.activities.LoginActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL="http://ec2-52-206-109-241.compute-1.amazonaws.com/team2practo/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain -> intercept(chain) })

    private fun intercept(chain: Interceptor.Chain): Response {
        return if(LoginActivity.TOKEN!=null) {
            val request = chain.request().newBuilder().addHeader("Authorization",
                LoginActivity.TOKEN!!
            ).build()
            chain.proceed(request)
        } else {
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}