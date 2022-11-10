package com.example.foodstabook.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivitySuggestionMainBinding
import com.example.foodstabook.model.RetrofitInstance
import kotlinx.android.synthetic.main.activity_reset_password.view.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.example.foodstabook.data.recipesList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


private val titleText = mutableStateOf("")
private val summaryText = mutableStateOf("")
private val ingredientsText = mutableStateOf("")
private val instructionsText = mutableStateOf("")
private val recipeImageUrl = mutableStateOf("")
private val screenState = mutableStateOf(0)
private val suggestionRequested = mutableStateOf(false)
class FoodSuggestionScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySuggestionMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeBuilder()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun RecipeBuilder() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val url by recipeImageUrl
    val title by titleText
    val summary by summaryText
    val ingredients by ingredientsText
    val instructions by instructionsText
    val state by screenState
    val suggestionRequest by suggestionRequested
    val coroutineScope = rememberCoroutineScope()

    if (state == 0) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cooking), contentDescription = null,
                contentScale = ContentScale.FillWidth, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 16.dp)
            ) {
                Button(
                    shape = RoundedCornerShape(24.dp),
                    onClick = {
                        screenState.value = -1
                        suggestionRequested.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFe69500)
                    ),
                    modifier = Modifier
                        .size(width = 185.dp, height = 60.dp)
                        .padding(bottom = 6.dp)
                )
                {
                    Text(
                        text = "Surprise me!",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    shape = RoundedCornerShape(24.dp),
                    onClick = { screenState.value = 1 },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFe69500)
                    ),
                    modifier = Modifier
                        .size(width = 185.dp, height = 60.dp)
                        .padding(bottom = 6.dp)
                )
                {
                    Text(
                        text = "Let Me Choose!",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (state == -1) {
        if (suggestionRequest) {
            LaunchedEffect(key1 = suggestionRequest) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                    val response = RetrofitInstance.spoonacularApi.getRandomRecipe(
                        false,
                        "6a802448b4a34e4daaeb8be9bfb8d399"
                    )
                    getRandomSuggestion(response)
                }
            }
            suggestionRequested.value = false
        }
        Column() {
            Text(
                text = "<",
                modifier = Modifier
                    .clickable { screenState.value = 0 }
                    .align(Alignment.Start)
                    .offset(8.dp),
                fontSize = 32.sp
            )
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
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = url),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .aspectRatio(1.35f)
                            .fillMaxWidth(1f)
                            .padding(horizontal = 8.dp)
                    )
                    Card(
                        backgroundColor = Color(0xFFF8D247),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = 10.dp
                    ) {
                        Column {
                            Text(
                                text = title,
                                color = Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = summary,
                                color = Black,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    Card(
                        backgroundColor = Color(0xFFF8D247),
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        elevation = 10.dp
                    ) {
                        Column {
                            Text(
                                text = "INGREDIENTS",
                                color = Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                textAlign = TextAlign.Start,
                            )
                            Text(
                                text = ingredients,
                                color = Black,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    Card(
                        backgroundColor = Color(0xFFF8D247),
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        elevation = 10.dp
                    ) {
                        Column {
                            Text(
                                text = "INSTRUCTIONS",
                                color = Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = instructions,
                                color = Black,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(buttons) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                        .padding(top = 4.dp),
                ) {

                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                suggestionRequested.value = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xFFe69500
                                )
                            ),
                            modifier = Modifier
                                .size(width = 175.dp, height = 68.dp)
                                .padding(bottom = 6.dp)
                        )
                        {
                            Text(
                                text = "Give Me Another!",
                                color = White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                }
            }
        }
    }
    if (state == 1) {
        Column() {
            Text(
                text = "<",
                modifier = Modifier
                    .clickable { screenState.value = 0 }
                    .align(Alignment.Start)
                    .offset(8.dp),
                fontSize = 32.sp
            )
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Quick Search",
                        modifier = Modifier.offset(8.dp),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "+ Filter",
                        modifier = Modifier.offset(x = (-8).dp),
                        fontSize = 24.sp,
                        color = DarkGray,
                        textAlign = TextAlign.End
                    )
                }
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 150.dp), modifier = Modifier
                        .fillMaxWidth()
                        .size(width = 300.dp, height = 300.dp)
                ) {
                    items(count = 12) {
                        Card(modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            elevation = 10.dp,
                            onClick = {}) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.borscht),
                                    contentDescription = null
                                )
                                Text(
                                    text = "TESTING",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    color = Black
                                )
                            }
                        }
                    }
                }
                Text(
                    text = "Preferences",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(8.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            }
            Text(
                text = "I want: ",
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black
            )
            Button(
                shape = RoundedCornerShape(24.dp),
                onClick = { screenState.value = 2 },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF1EDF2)
                ),
                modifier = Modifier.offset(x = 8.dp)
            ) {
                Text(
                    text = "+",
                    color = Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "I don't want: ",
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black
             )
            Button(
                shape = RoundedCornerShape(24.dp),
                onClick = { screenState.value = 2 },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF1EDF2)
                ),
                modifier = Modifier.offset(x = 8.dp)
            ) {
                Text(
                    text = "+",
                    color = Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    if (state == 2){
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()){
                Text(
                    text = "<",
                    modifier = Modifier
                        .clickable { screenState.value = 1 }
                        .offset(8.dp),
                    fontSize = 32.sp
                )
                Button(
                    shape = RoundedCornerShape(24.dp),
                    onClick = {
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFF6482FF
                        )
                    ),
                    modifier = Modifier
                        .size(width = 100.dp, height = 40.dp)
                        .padding(top = 6.dp)
                        .offset(x = (-8).dp)
                )
                {
                    Text(
                        text = "Clear All",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
            }
            Text(text = "Preferences ",
                color = Black,
                fontWeight = FontWeight.Black,
                modifier = Modifier.offset(x = 8.dp),
                fontSize = 32.sp)
            Column() {

                Text(
                    text = "I want: ",
                    color = Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 8.dp),
                    fontSize = 20.sp
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(x = (-10).dp)
                ) {
                    items(count = 12) {
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF1EDF2)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 100.dp, height = 40.dp)
                        ) {
                            Text(
                                text = "TESTING",
                                color = Gray,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Column() {

                Text(
                    text = "I don't want: ",
                    color = Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 8.dp),
                    fontSize = 20.sp
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(x = (-10).dp)
                ) {
                    items(count = 12) {
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF1EDF2)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 100.dp, height = 40.dp)
                        ) {
                            Text(
                                text = "TESTING",
                                color = Gray,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom) {
                Button(
                    shape = RoundedCornerShape(24.dp),
                    onClick = {
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFFe69500
                        )
                    ),
                    modifier = Modifier
                        .size(width = 150.dp, height = 70.dp)
                        .padding(bottom = 6.dp)
                )
                {
                    Text(
                        text = "Serve Me!",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
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
fun RecipePreview() {
    MaterialTheme {
        RecipeBuilder()
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun getRandomSuggestion(response: Response<recipesList>){
        val ingredientsBuilder = StringBuilder()
        val instructionsBuilder = StringBuilder()
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
                (Html.fromHtml(responseBody.recipes[0].title, 0)
                    .toString())
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

/* Working on: html to text (need to include effects like bold/italics,
padding/spacing, generally making ui prettier
 */