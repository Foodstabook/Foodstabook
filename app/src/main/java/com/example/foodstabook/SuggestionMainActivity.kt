package com.example.foodstabook

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodstabook.databinding.ActivitySuggestionMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuggestionMainActivity : AppCompatActivity() {

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

    private lateinit var binding: ActivitySuggestionMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuggestionMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var pseudorandomId: Long = 0
        val titleSwitcher = binding.recipeName
        val summarySwitcher = binding.summary
        val ingredientsTitleCardSwitcher = binding.ingredientsTitleCard
        val ingredientsSwitcher = binding.recipeIngredients
        val instructionsTitleCardSwitcher = binding.instructionsTitleCard
        val instructionSwitcher = binding.recipeInstructions
        val imageSwitcher = binding.recipePicture

        titleSwitcher.setFactory {
            val textView = TextView(this@SuggestionMainActivity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 40f
            textView.setTextColor(Color.rgb(153,40,204))
            textView
        }

        summarySwitcher.setFactory {
            val textView = TextView(this@SuggestionMainActivity)
            textView.gravity = Gravity.START
            textView.textSize = 20f
            textView.setTextColor(Color.rgb(0,0,0))
            textView
        }

        ingredientsTitleCardSwitcher.setFactory {
            val textView = TextView(this@SuggestionMainActivity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 30f
            textView.setTextColor(Color.rgb(153,40,204))
            textView
        }

        ingredientsSwitcher.setFactory {
            val textView = TextView(this@SuggestionMainActivity)
            textView.gravity = Gravity.START
            textView.textSize = 20f
            textView.setTextColor(Color.rgb(0,0,0))
            textView
        }

        instructionsTitleCardSwitcher.setFactory {
            val textView = TextView(this@SuggestionMainActivity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 30f
            textView.setTextColor(Color.rgb(153,40,204))
            textView
        }

        instructionSwitcher.setFactory {
            val textView = TextView(this@SuggestionMainActivity)
            textView.gravity = Gravity.START
            textView.textSize = 20f
            textView.setTextColor(Color.rgb(0,0,0))
            textView
        }

        imageSwitcher.setFactory{
            val imageView = ImageView(this@SuggestionMainActivity)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView
        }

        val animIn = AnimationUtils.loadAnimation(
            this, android.R.anim.fade_in)
        animIn.duration = 600
        titleSwitcher.inAnimation = animIn
        summarySwitcher.inAnimation = animIn
        ingredientsTitleCardSwitcher.inAnimation = animIn
        ingredientsSwitcher.inAnimation = animIn
        instructionsTitleCardSwitcher.inAnimation = animIn
        instructionSwitcher.inAnimation = animIn
        imageSwitcher.inAnimation = animIn

        val animOut = AnimationUtils.loadAnimation(
            this, android.R.anim.fade_out)
        animOut.duration = 150
        titleSwitcher.outAnimation = animOut
        summarySwitcher.outAnimation = animOut
        ingredientsTitleCardSwitcher.outAnimation = animOut
        ingredientsSwitcher.outAnimation = animOut
        instructionsTitleCardSwitcher.outAnimation = animOut
        instructionSwitcher.outAnimation = animOut
        imageSwitcher.outAnimation = animOut

        titleSwitcher.setText(recipeList[0])
        imageSwitcher.setImageResource(imageList[0])
        instructionSwitcher.setText(getString(instructionsList[0]))

        /*binding.randomButton.setOnClickListener{
            newIndex = (1 until (recipeList.size)).random()
            while (newIndex == index)
                newIndex = (1 until (recipeList.size)).random()
            imageSwitcher.setImageResource(imageList[newIndex])
            instructionSwitcher.setText(getString(instructionsList[newIndex]))
            index = newIndex
        }*/

        binding.randomButton.setOnClickListener{
            newIndex = (1 until (recipeList.size)).random()
            while (newIndex == index)
                newIndex = (1 until (recipeList.size)).random()
            when (newIndex) {
                1 -> 100777
                2 -> 1
                3 -> 100
                4 -> 200
                5 -> 2
                6 -> 491
                7 -> 42
                8 -> 28471
                9 -> 68299
                10 -> 629001
                else -> {
                    0
                }
            }.also { pseudorandomId = it.toLong() }
            CoroutineScope(Dispatchers.Main).launch {
                val ingredientsBuilder = StringBuilder()
                val instructionsBuilder = StringBuilder()
                val response = RetrofitInstance.spoonacularApi.getRecipeInfo(pseudorandomId,
                    false,"e16c808bc22e41ceb1cc4f18180159f5")
                val responseBody = response.body()!!
                val recipeImage = responseBody.image
                Glide.with(this@SuggestionMainActivity)
                    .asDrawable()
                    .load(recipeImage)
                    .into(imageSwitcher.nextView as ImageView)
                responseBody.extendedIngredients.forEach{
                    ingredientsBuilder.append(it.amount.toString()+" "+it.unit+" "
                            +it.nameClean+"\n")
                }

                responseBody.analyzedInstructions.forEach { it1 ->
                    if (it1.steps.isNotEmpty()) {
                        it1.steps.forEach {
                            instructionsBuilder.append(it.number.toString() + ". " + it.step + "\n")
                        }
                    }
                    else
                        instructionsBuilder.append("")
                }
                titleSwitcher.setText(responseBody.title)
                summarySwitcher.setText(responseBody.summary)
                ingredientsTitleCardSwitcher.setText("INGREDIENTS:")
                ingredientsSwitcher.setText(ingredientsBuilder)
                instructionsTitleCardSwitcher.setText("INSTRUCTIONS:")
                instructionSwitcher.setText(instructionsBuilder)
                imageSwitcher.showNext()
                binding.scrollView.smoothScrollTo(0,0)
            }
        }

        binding.tailoredButton.setOnClickListener{
            titleSwitcher.setText(getString(R.string.wip_title))
            summarySwitcher.setText(getString(R.string.wip_summary))
            ingredientsTitleCardSwitcher.setText("INGREDIENTS:")
            ingredientsSwitcher.setText("A lot of:\nSweat\nTears\nSleepless Nights")
            instructionsTitleCardSwitcher.setText("INSTRUCTIONS:")
            instructionSwitcher.setText("This is might take a while...")
            imageSwitcher.setImageResource(imageList[0])
            binding.scrollView.smoothScrollTo(0,0)
        }

        goHome()
    }

    private fun goHome(){
        binding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}