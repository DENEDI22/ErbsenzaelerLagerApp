package com.example.team6mobileapp.network

import com.example.team6mobileapp.model.Artikel
import com.example.team6mobileapp.model.ArtikelResponse
import com.example.team6mobileapp.model.ArtikelUpdateRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


//Is here in case someone fixes our broken backend...
interface ArtikelApiService {
    @GET("artikel") // Adjust if you have a specific path
    suspend fun getArtikel(): List<Artikel>

    @POST("artikel")
    suspend fun createArtikel(@Body request: com.example.team6mobileapp.model.ArtikelCreateRequest): ArtikelResponse

    @PUT("artikel/{nr}")
    suspend fun updateArtikel(@Path("nr") nr: Int, @Body request: ArtikelUpdateRequest): ArtikelResponse

    companion object {
        private const val BASE_URL = "http:/212.227.79.83/api/" // Placeholder

        fun create(): ArtikelApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ArtikelApiService::class.java)
        }
    }
}