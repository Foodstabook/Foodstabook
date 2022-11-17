package com.example.foodstabook.model

import retrofit2.http.GET

interface cloudFunctionsInterface{
    @GET("callSearchLatestPosts")
    suspend fun getPosts()
}