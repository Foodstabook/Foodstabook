package com.example.randomfoodsuggestionprototype


import android.R.*
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.randomfoodsuggestionprototype.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var index = 0
    private var newIndex = -1
    private val recipeList = mutableListOf("???", "Shrimp al Mojo de Ajo", "Ratatouille",
        "Blueberry Crumble", "Ochazuke", "Vegetable Terrine", "Chicken Tikka Masala", "Borscht",
        "Kimchi-jjigae", "Falafel", "Bunny Chow")
    private val imageList = intArrayOf(R.drawable.red_question_mark, R.drawable.shrimp_al_mojo_de_ajo,
    R.drawable.ratatouille, R.drawable.blueberry_crumble, R.drawable.ochazuke, R.drawable.vegetable_terrine,
    R.drawable.chicken_tikka_masala, R.drawable.borscht, R.drawable.kimchi_jjigae, R.drawable.falafel,
    R.drawable.bunny_chow)

    private val instructionsList = intArrayOf(R.string.blank_instructions, R.string.shrimp_al_mojo_de_ajo_instructions,
        R.string.ratatouille_instructions, R.string.blueberry_crumble_instructions, R.string.ochazuke_instructions,
        R.string.vegetable_terrine_instructions, R.string.chicken_tikka_masala_instructions,
        R.string.borscht_instructions, R.string.kimchi_jjigae_instructions, R.string.falafel_instructions,
        R.string.bunny_chow_instructions)

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val textSwitcher = binding.recipeName
        val imageSwitcher = binding.recipePicture
        val instructionSwitcher = binding.recipeInstructions

        textSwitcher.setFactory {
            val textView = TextView(this@MainActivity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 40f
            textView.setTextColor(Color.rgb(153,40,204))
            textView
        }

        imageSwitcher.setFactory{
            val imageView = ImageView(this@MainActivity)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView
        }

        instructionSwitcher.setFactory {
            val textView = TextView(this@MainActivity)
            textView.gravity = Gravity.START
            textView.textSize = 20f
            textView.setTextColor(Color.rgb(0,0,0))
            textView
        }
        textSwitcher.setText(recipeList[0])
        imageSwitcher.setImageResource(imageList[0])
        instructionSwitcher.setText(getString(instructionsList[0]))
        binding.button.setOnClickListener{
            newIndex = (1 until (recipeList.size)).random()
            while (newIndex == index)
                newIndex = (1 until (recipeList.size)).random()
            textSwitcher.setText(recipeList[newIndex])
            imageSwitcher.setImageResource(imageList[newIndex])
            instructionSwitcher.setText(getString(instructionsList[newIndex]))
            index = newIndex
        }
        val animIn = AnimationUtils.loadAnimation(
            this, anim.slide_in_left)
        textSwitcher.inAnimation = animIn
        imageSwitcher.inAnimation = animIn
        instructionSwitcher.inAnimation = animIn

        val animOut = AnimationUtils.loadAnimation(
            this, anim.slide_out_right)
        textSwitcher.outAnimation = animOut
        imageSwitcher.outAnimation = animOut
        instructionSwitcher.outAnimation = animOut
    }

   /* private fun generateRecipe(){
        binding.button.setOnClickListener {
            val random = (0 until (recipeList.size)).random()

        }
    } */

}