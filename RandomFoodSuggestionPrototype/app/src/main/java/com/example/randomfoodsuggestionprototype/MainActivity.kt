package com.example.randomfoodsuggestionprototype


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.randomfoodsuggestionprototype.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val recipeList = mutableListOf("Shrimp al Mojo de Ajo", "Ratatouille",
        "Blueberry Crumble", "Ochazuke", "Vegetable Terrine", "Chicken Tikka Masala", "Borscht",
        "Kimchi-jjigae", "Falafel", "Bunny Chow")

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        generateRecipe()
    }

    private fun generateRecipe(){
        binding.button.setOnClickListener {
            val random = (0 until (recipeList.size)).random()
            binding.recipeTest.text = recipeList[random]
        }
    }

}