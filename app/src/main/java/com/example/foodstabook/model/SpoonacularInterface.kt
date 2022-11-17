package com.example.foodstabook.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.foodstabook.data.*
import retrofit2.http.Path

interface SpoonacularInterface {

    @GET("recipes/random?")
    suspend fun getRandomRecipe(@Query("includeNutrition")includeNutrition: Boolean,
                                @Query("apiKey")apiKey:String
    ) : Response<RandomRecipesList>

    @GET("recipes/complexSearch")
    suspend fun getTailoredSuggestions(@Query("includeNutrition")includeNutrition: Boolean,
                                       @Query("addRecipeInformation")addRecipeInformation: Boolean,
                                       @Query("includeIngredients")includeIngredients: String,
                                       @Query("excludeIngredients")excludeIngredients: String,
                                       @Query("diet")diet: String,
                                       @Query("intolerances")intolerances: String,
                                       @Query("instructionsRequired")instructionsRequired: Boolean,
                                       @Query("type")type: String,
                                       @Query("cuisine")cuisine: String,
                                       @Query("number")number: Number,
                                       @Query("sort")sort: String,
                                       @Query("apiKey")apiKey:String,
    ): Response<TailoredRecipesList>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInfo(@Path("id")id: Number,
                              @Query("includeNutrition")includeNutrition: Boolean,
                              @Query("apiKey")apiKey: String
    ): Response<Recipe>
}