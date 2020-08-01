package com.my.kotlinrecyclerview

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {

    @GET("volley_array.json")
    fun getMovies(): Call<List<Movie>>

    @GET("GetContractorsByOwner?")
    fun getContractorList(@Query("customerId") customerId : String?, @Header("Authorization") authHeaderToken: String?) : Call<String>


    companion object {

        //https://test-ai-joc-api.gordiancloud.com/v1.0/JobOrders/GetContractorsByOwner?customerId=243821
        var BASE_URL = "https://test-ai-joc-api.gordiancloud.com/v1.0/JobOrders/"
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}