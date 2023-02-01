package com.example.android.marsphotos.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {

    @GET("photos")
    suspend fun getPhotos(): String
}

/*
* Singleton pattern in kotlin
* Each time app calls MarsApi.retrofitService, the caller will access the same singleton Retrofit object
* that implements MarsApiService which is created on the first access by lazy
*/

object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}