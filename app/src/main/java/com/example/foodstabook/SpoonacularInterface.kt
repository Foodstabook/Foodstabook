package com.example.foodstabook

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularInterface {

    @GET("recipes/{id}/information?")
    suspend fun getRecipeInfo(@Path("id")id: Long, @Query("includeNutrition")includeNutrition: Boolean,
    @Query("apiKey")apiKey:String) : Response<recipeTest>
}