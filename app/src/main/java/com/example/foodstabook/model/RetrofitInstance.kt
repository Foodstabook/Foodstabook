package com.example.foodstabook.model

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    val spoonacularApi: SpoonacularInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SpoonacularInterface::class.java)
    }
}
