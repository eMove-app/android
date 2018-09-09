package com.emove.emove.retrofit

import com.emove.emove.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST

interface EmoveApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("token") token: String): Call<LoginResponse>

    @GET("me")
    fun getUser(@Header("x-token") token: String): Call<GetUserResponse>

    @FormUrlEncoded
    @POST("me")
    fun updateUser(@Header("x-token") token: String, @Body user: User): Call<ResponseBody>

//    @FormUrlEncoded
//    @POST("car")
//    fun updateCarUserInfo(@Field("token") token: String): Call<LoginResponse>
//
//    @FormUrlEncoded
//    @POST("addresses")
//    fun updateAddressesUserInfo(@Field("token") token: String): Call<LoginResponse>

    @POST("rides/action/start")
    fun startTrip(@Header("x-token") token: String, @Body body: StartTripBody): Call<SearchResultsResponse>

    @GET("find-ride")
    fun findTrip(@Header("x-token") token: String, @Query("lat") lat: Double, @Query("lng") lng: Double): Call<SearchResultsResponse>

    @GET("rides/{" + "id" + "}")
    fun getTrip(@Header("x-token") token: String, @Path("id") id: Int): Call<GetTripResponse>

}