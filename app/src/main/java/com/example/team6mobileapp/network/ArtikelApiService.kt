package com.example.team6mobileapp.network

import com.example.team6mobileapp.model.Artikel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ArtikelApiService {
    @GET("artikel") // Adjust if you have a specific path
    suspend fun getArtikel(): List<Artikel>

    companion object {
        private const val BASE_URL = "http://212.227.79.83/api/" // Placeholder

        fun create(): ArtikelApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ArtikelApiService::class.java)
        }
    }
}