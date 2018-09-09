package com.emove.emove.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EmoveApi {

    val instance: EmoveApiService

    init {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://167.99.32.81:5000/")
                .build()

        instance = retrofit.create(EmoveApiService::class.java)
    }
}