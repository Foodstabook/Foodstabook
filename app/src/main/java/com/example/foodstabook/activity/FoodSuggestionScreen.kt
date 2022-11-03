package com.example.foodstabook.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivitySuggestionMainBinding
import com.example.foodstabook.model.RetrofitInstance
import kotlinx.android.synthetic.main.activity_reset_password.view.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch


val titleText = mutableStateOf("")
val summaryText = mutableStateOf("")
val ingredientsTitleText = mutableStateOf("")
val ingredientsText = mutableStateOf("")
val instructionsTitleText = mutableStateOf("")
val instructionsText = mutableStateOf("")
val recipeImageUrl = mutableStateOf("")
class FoodSuggestionScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySuggestionMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            recipeBuilder()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun recipeBuilder() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val url by recipeImageUrl
    val title by titleText
    val summary by summaryText
    val ingredientsTitle by ingredientsTitleText
    val ingredients by ingredientsText
    val instructionsTitle by instructionsTitleText
    val instructions by instructionsText
    val coroutineScope = rememberCoroutineScope()

    Column() {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (buttons, recipeInfo) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(recipeInfo) {
                        top.linkTo(parent.top)
                        bottom.linkTo(buttons.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = url),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .size(350.dp, 350.dp)
                )
                Text(
                    text = title,
                    color = androidx.compose.ui.graphics.Color(0xFF9928CC),
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = summary,
                    color = Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = ingredientsTitle,
                    color = androidx.compose.ui.graphics.Color(0xFF9928CC),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = ingredients,
                    color = Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = instructionsTitle,
                    color = androidx.compose.ui.graphics.Color(0xFF9928CC),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = instructions,
                    color = Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Start
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttons) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    },
            ) {
                ConstraintLayout() {

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(0)
                                val ingredientsBuilder = StringBuilder()
                                val instructionsBuilder = StringBuilder()
                                val response = RetrofitInstance.spoonacularApi.getRandomRecipe(
                                    false,
                                    "6a802448b4a34e4daaeb8be9bfb8d399"
                                )
                                val responseBody = response.body()
                                val newImage = responseBody?.recipes?.get(0)?.image
                                responseBody?.recipes?.get(0)?.extendedIngredients?.forEach {
                                    ingredientsBuilder.append(
                                        it.amount.toString() + " " + it.unit + " "
                                                + it.nameClean + "\n"
                                    )
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
                                    titleText.value =
                                        (Html.fromHtml(responseBody.recipes[0].title, 0).toString())
                                }
                                if (responseBody != null) {
                                    summaryText.value =
                                        (Html.fromHtml(responseBody.recipes[0].summary, 0)
                                            .toString())
                                }
                                instructionsText.value = instructionsBuilder.toString()
                                ingredientsText.value = ingredientsBuilder.toString()
                                if (newImage != null) {
                                    recipeImageUrl.value = newImage
                                }
                            }
                            ingredientsTitleText.value = "INGREDIENTS"
                            instructionsTitleText.value = "INSTRUCTIONS"
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = androidx.compose.ui.graphics.Color(
                                0xFFe69500
                            )
                        ),
                        modifier = Modifier
                            .size(width = 125.dp, height = 75.dp)
                            .padding(bottom = 6.dp)
                    )
                    {
                        Text(
                            text = "Random",
                            color = Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            titleText.value = context.getString(R.string.wip_title)
                            summaryText.value = context.getString(R.string.wip_summary)
                            ingredientsTitleText.value = "INGREDIENTS:"
                            ingredientsText.value = "A lot of:\nSweat\nTears\nSleepless Nights"
                            instructionsTitleText.value = "INSTRUCTIONS:"
                            instructionsText.value = "This might take a while..."
                            recipeImageUrl.value = ""
                            scrollState.animateScrollTo(0)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = androidx.compose.ui.graphics.Color(
                            0xFFe69500
                        )
                    ),
                    modifier = Modifier
                        .size(width = 125.dp, height = 75.dp)
                        .padding(bottom = 6.dp)
                )
                {
                    Text(
                        text = "Tailored",
                        color = Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun recipePreview() {
    MaterialTheme {
        recipeBuilder()
    }
}

/* Working on: html to text (need to include effects like bold/italics,
padding/spacing, generally making ui prettier
 */