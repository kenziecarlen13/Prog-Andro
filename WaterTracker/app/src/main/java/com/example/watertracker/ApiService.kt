package com.example.watertracker

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/quotes")
    fun getRandomQuotes(): Call<List<Quote>>
}
