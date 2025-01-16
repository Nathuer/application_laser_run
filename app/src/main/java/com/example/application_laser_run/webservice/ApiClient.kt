package com.example.application_laser_run.webservice

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL: String = "https://kahriboo.com/univ/"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpsClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpsClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}