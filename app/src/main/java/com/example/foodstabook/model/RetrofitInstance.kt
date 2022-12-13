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

    val cloudFunctionsApi: CloudFunctionsInterface by lazy{
        Retrofit.Builder()
            .baseUrl("https://https://us-central1-foodstabook-a34b4.cloudfunctions.net/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CloudFunctionsInterface::class.java)
    }
}
