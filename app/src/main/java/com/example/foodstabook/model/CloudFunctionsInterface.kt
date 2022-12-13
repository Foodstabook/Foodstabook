package com.example.foodstabook.model

import retrofit2.http.GET

interface CloudFunctionsInterface{
    @GET("getAllPosts")
    suspend fun getAllPosts()
}