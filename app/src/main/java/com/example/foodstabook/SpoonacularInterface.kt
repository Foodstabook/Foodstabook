package com.example.foodstabook

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.foodstabook.data.*

interface SpoonacularInterface {

    @GET("recipes/random?")
    suspend fun getRandomRecipe(@Query("includeNutrition")includeNutrition: Boolean,
    @Query("apiKey")apiKey:String) : Response<recipesList>
}