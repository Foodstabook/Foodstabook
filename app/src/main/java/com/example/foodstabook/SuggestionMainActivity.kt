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
import com.example.foodstabook.activity.MainActivity

class SuggestionMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuggestionMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuggestionMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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

        titleSwitcher.setText("???")
        imageSwitcher.setImageResource(R.drawable.red_question_mark)
        instructionSwitcher.setText("")

        binding.randomButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val ingredientsBuilder = StringBuilder()
                val instructionsBuilder = StringBuilder()
                val response = RetrofitInstance.spoonacularApi.getRandomRecipe(false,
                    "6a802448b4a34e4daaeb8be9bfb8d399")
                val responseBody = response.body()
                val recipeImage = responseBody?.recipes?.get(0)?.image
                Glide.with(this@SuggestionMainActivity)
                    .asDrawable()
                    .load(recipeImage)
                    .into(imageSwitcher.nextView as ImageView)
                responseBody?.recipes?.get(0)?.extendedIngredients?.forEach{
                    ingredientsBuilder.append(it.amount.toString()+" "+it.unit+" "
                            +it.nameClean+"\n")
                }

                responseBody?.recipes?.get(0)?.analyzedInstructions?.forEach { it1 ->
                    if (it1.steps.isNotEmpty()) {
                        it1.steps.forEach {
                            instructionsBuilder.append(it.number.toString() + ". " + it.step + "\n")
                        }
                    } else
                        instructionsBuilder.append("")
                }
                if (responseBody != null) {
                    titleSwitcher.setText(responseBody.recipes[0].title)
                }
                if (responseBody != null) {
                    summarySwitcher.setText(responseBody.recipes[0].summary)
                }
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
            imageSwitcher.setImageResource(R.drawable.red_question_mark)
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