package com.example.epact.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Se estiver no emulador, use 10.0.2.2. Se for celular real, use seu IP.
    private const val BASE_URL = "http://10.1.1.39:1337/"

    val instance: StrapiApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(StrapiApiService::class.java)
    }
}