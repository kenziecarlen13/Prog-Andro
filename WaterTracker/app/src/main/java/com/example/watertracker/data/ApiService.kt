package com.example.watertracker.data

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Contoh pemanggilan API. Ganti dengan endpoint Anda yang sebenarnya.
    @GET("posts/1")
    suspend fun getPlaceholderData(): Response<Any>
}
