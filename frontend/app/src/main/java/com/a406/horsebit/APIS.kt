package com.a406.horsebit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIS {

    @POST("/api/exchange/orders")
    fun notConcluded(
        @Body requestBody: NotConcludedRequestBodyModel,
    ): Call<NotConcludedResponseBodyModel>

    companion object {
        private const val BASE_URL = "https://j9a406.p.ssafy.io"

        fun create(): APIS {
            val gson : Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}