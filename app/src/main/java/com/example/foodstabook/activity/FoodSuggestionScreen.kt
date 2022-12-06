package com.example.foodstabook.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivitySuggestionMainBinding
import com.example.foodstabook.model.RetrofitInstance
import kotlinx.android.synthetic.main.activity_reset_password.view.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.example.foodstabook.data.RandomRecipesList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalConfiguration
import com.example.foodstabook.data.Recipe
import com.example.foodstabook.data.Result
import com.example.foodstabook.data.TailoredRecipesList
import com.example.foodstabook.model.DietsSelectionModel
import com.example.foodstabook.model.PreferencesSelectionModel
import com.example.foodstabook.model.QuickSelectionModel
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import retrofit2.Response


private val screenState: MutableState<Int> = mutableStateOf(0)
private val titleText: MutableState<String> = mutableStateOf("")
private val summaryText: MutableState<String> = mutableStateOf("")
private val ingredientsText: MutableState<String> = mutableStateOf("")
private val instructionsText: MutableState<String> = mutableStateOf("")
private val recipeImageUrl: MutableState<String> = mutableStateOf("")
private val wantsList: MutableState<List<PreferencesSelectionModel>> =
    mutableStateOf(generateIngredients())
private val selectedWantsList: SnapshotStateList<PreferencesSelectionModel> =
    mutableStateListOf()
private val doNotWantsList: MutableState<List<PreferencesSelectionModel>> =
    mutableStateOf(generateIngredients())
private val selectedDoNotWantsList: SnapshotStateList<PreferencesSelectionModel> =
    mutableStateListOf()
private val dietsList: MutableState<List<DietsSelectionModel>> =
    mutableStateOf(generateDiets())
private val selectedDietsList: SnapshotStateList<DietsSelectionModel> =
    mutableStateListOf()
private val intolerancesList: MutableState<List<PreferencesSelectionModel>> =
    mutableStateOf(generateIntolerances())
private val selectedIntolerancesList: SnapshotStateList<PreferencesSelectionModel> =
    mutableStateListOf()
private val quickSelectList: MutableState<List<QuickSelectionModel>> =
    mutableStateOf(generateQuickSelection())
private val quickSelectFilter: SnapshotStateList<QuickSelectionModel> =
    mutableStateListOf()
private val quickSelectedItem: SnapshotStateList<QuickSelectionModel> =
    mutableStateListOf()
private var recipeList: List<Result>? = listOf()
private val recipeListPosition: MutableState<Int> = mutableStateOf(0)


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
    val scrollState = rememberScrollState()
    val url by recipeImageUrl
    val title by titleText
    val summary by summaryText
    val ingredients by ingredientsText
    val instructions by instructionsText
    val wants by wantsList
    val doNotWants by doNotWantsList
    val diets by dietsList
    val intolerances by intolerancesList
    val quickSelect by quickSelectList
    val state by screenState
    val suggestionListRequest = remember { mutableStateOf(false) }
    var displayResponse: Response<Recipe>
    val changeRequested = remember { mutableStateOf(false) }
    val recipePosition by recipeListPosition
    val noRecipeResults = remember { mutableStateOf(false)}
    val quickSelectRequest = remember{mutableStateOf(false)}
    val coroutineScope = rememberCoroutineScope()

    // Initial Screen
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
                        suggestionListRequest.value = true
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

    // Random Food Suggestions
    if (state == -1) {
        if (suggestionListRequest.value) {
            LaunchedEffect(key1 = suggestionListRequest.value) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                    val response = RetrofitInstance.spoonacularApi.getRandomRecipe(
                        false,
                        "6a802448b4a34e4daaeb8be9bfb8d399"
                    )

                    displayRandomSuggestion(response)
                }
            }
            suggestionListRequest.value = false
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
                            suggestionListRequest.value = true
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
    // Quick Select/Selected Preferences Screen
    if (state == 1) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "<",
                modifier = Modifier
                    .clickable { screenState.value = 0 }
                    .align(Alignment.Start)
                    .offset(8.dp),
                fontSize = 32.sp
            )
            Column(modifier = Modifier.verticalScroll(scrollState)
                .weight(1f)) {
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
                        modifier = Modifier.offset(x = (-8).dp)
                            .clickable { screenState.value = 2 },
                        fontSize = 24.sp,
                        color = DarkGray,
                        textAlign = TextAlign.End
                    )
                }
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 130.dp), modifier = Modifier
                        .fillMaxWidth()
                        .size(width = 300.dp, height = 260.dp)
                ) {
                    items(count = quickSelectFilter.size) {item->
                        Card(modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            elevation = 10.dp,
                            onClick = {quickSelectRequest.value = true
                                suggestionListRequest.value = true
                                screenState.value = 4
                                quickSelectFilter[item].isSelected.value = true
                                quickSelectedItem.add(quickSelectFilter[item])

                            }) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = quickSelectFilter[item].image),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.requiredHeight(130.dp)
                                        .requiredWidth(150.dp)
                                )
                                Box(modifier = Modifier.matchParentSize()
                                    .background(Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, DarkGray))))
                                Text(
                                    text = quickSelectFilter[item].title,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                        .requiredWidth(150.dp),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    color = White
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
                Text(
                    text = "Tap to Remove Selected Preferences",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
                Text(
                    text = "Ingredients I want: ",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 60.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(x = (-12).dp)
                        .size(width = 0.dp, height = 190.dp)
                ) {
                    item {
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = { screenState.value = 3 },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF1EDF2)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = "+",
                                color = Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(count = selectedWantsList.size) { item ->
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                val index = wantsList.value.indexOf(selectedWantsList[item])
                                wantsList.value[index].isSelected.value = false
                                selectedWantsList.remove(selectedWantsList[item])
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFe69500)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = selectedWantsList[item].title,
                                color = White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Text(
                    text = "Ingredients I Don't Want: ",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 60.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(x = (-12).dp)
                        .size(width = 0.dp, height = 190.dp)
                ) {
                    item {
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = { screenState.value = 3 },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF1EDF2)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = "+",
                                color = Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(count = selectedDoNotWantsList.size) { item ->
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                val index =
                                    doNotWantsList.value.indexOf(selectedDoNotWantsList[item])
                                doNotWantsList.value[index].isSelected.value = false
                                selectedDoNotWantsList.remove(selectedDoNotWantsList[item])
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFe69500)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = selectedDoNotWantsList[item].title,
                                color = White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Text(
                    text = "Diets: ",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 60.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(x = (-12).dp)
                        .size(width = 0.dp, height = 190.dp)
                ) {
                    item {
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = { screenState.value = 3},
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF1EDF2)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = "+",
                                color = Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(count = selectedDietsList.size) { item ->
                        val buttonColor = if (selectedDietsList[item].ORisSelected.value && !selectedDietsList[item].ANDisSelected.value) {
                            Color(0xFF30B44E)
                        }
                        else if(!selectedDietsList[item].ORisSelected.value && selectedDietsList[item].ANDisSelected.value){
                            Color(0xFFe69500)
                        }
                        else{Color(0xFF30B44E)}
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                val index =
                                    dietsList.value.indexOf(selectedDietsList[item])
                                dietsList.value[index].ORisSelected.value = false
                                dietsList.value[index].ANDisSelected.value = false
                                selectedDietsList.remove(selectedDietsList[item])
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = buttonColor
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = selectedDietsList[item].title,
                                color = White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Text(
                    text = "Intolerances: ",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .offset(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 60.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .offset(x = (-12).dp)
                        .size(width = 0.dp, height = 190.dp)
                ) {
                    item {
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = { screenState.value = 3 },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF1EDF2)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = "+",
                                color = Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(count = selectedIntolerancesList.size) { item ->
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                val index =
                                    intolerancesList.value.indexOf(selectedIntolerancesList[item])
                                intolerancesList.value[index].isSelected.value = false
                                selectedIntolerancesList.remove(selectedIntolerancesList[item])
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFe69500)
                            ),
                            modifier = Modifier
                                .offset(x = 8.dp)
                                .padding(6.dp)
                                .size(width = 125.dp, height = 30.dp)
                        ) {
                            Text(
                                text = selectedIntolerancesList[item].title,
                                color = White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    shape = RoundedCornerShape(24.dp),
                    onClick = {
                        suggestionListRequest.value = true
                        screenState.value = 4
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFFe69500
                        )
                    ),
                    modifier = Modifier
                        .size(width = 150.dp, height = 60.dp)
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
    // Quick Search Filter Screen
    if (state == 2){
        val grayScale = ColorMatrix()
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                        for (element in quickSelect) {
                            element.isSelected.value = false
                        }
                        quickSelectFilter.clear()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFF6482FF
                        )
                    ),
                    modifier = Modifier
                        .size(width = 130.dp, height = 60.dp)
                        .padding(top = 6.dp)
                        .offset(x = (-8).dp)
                )
                {
                    Text(
                        text = "Clear All",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

            }
            Column(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp)) {
                Text(
                    text = "Quick Search Filter",
                    color = Black,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(x = 8.dp),
                    fontSize = 32.sp
                )
                LazyHorizontalGrid(
                    rows = GridCells.Adaptive(minSize = 180.dp), modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(count = quickSelect.size) {item->
                        if(quickSelect[item].isSelected.value)
                            grayScale.setToSaturation(1f)
                        else
                            grayScale.setToSaturation(0f)
                        Card(modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .fillMaxSize(),
                            shape = RoundedCornerShape(30.dp),
                            elevation = 10.dp,
                        onClick = {quickSelect[item].isSelected.value = !(quickSelect[item].isSelected.value)
                            if (quickSelect[item].isSelected.value) {
                                quickSelectFilter.add(quickSelect[item])
                                quickSelectFilter.sortBy { it.title }
                            } else {
                                quickSelectFilter.remove(quickSelect[item])
                            }}){
                            Box(modifier = Modifier.fillMaxHeight()
                                ) {
                                Image(
                                    painter = painterResource(id = quickSelect[item].image),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.requiredWidth(180.dp)
                                        .requiredHeight(200.dp),
                                    colorFilter = ColorFilter.colorMatrix(grayScale)
                                )
                                Box(modifier = Modifier.matchParentSize()
                                    .background(Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, DarkGray))))
                                Text(
                                    text = quickSelect[item].title,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                        .requiredWidth(180.dp),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 32.sp,
                                    color = White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // Detailed Preference Selection Screen
    if (state == 3) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                        for (element in wants) {
                            element.isSelected.value = false
                        }
                        for (element in doNotWants) {
                            element.isSelected.value = false
                        }
                        for (element in diets){
                            element.ORisSelected.value = false
                            element.ANDisSelected.value = false
                        }
                        for (element in intolerances){
                            element.isSelected.value = false
                        }
                        selectedWantsList.clear()
                        selectedDoNotWantsList.clear()
                        selectedDietsList.clear()
                        selectedIntolerancesList.clear()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFF6482FF
                        )
                    ),
                    modifier = Modifier
                        .size(width = 130.dp, height = 60.dp)
                        .padding(top = 6.dp)
                        .offset(x = (-8).dp)
                )
                {
                    Text(
                        text = "Clear All",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(modifier = Modifier.verticalScroll(scrollState)
                .weight(1f)) {
                Text(
                    text = "Preferences ",
                    color = Black,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(x = 8.dp),
                    fontSize = 32.sp
                )
                Text(
                    text = "Orange means AND, Green means OR ",
                    color = Black,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(x = 8.dp),
                    fontSize = 20.sp
                )
                //Wants Selection
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ingredients I Want: ",
                            color = Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = 8.dp),
                            fontSize = 20.sp
                        )
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                for (element in wants) {
                                    element.isSelected.value = false
                                }
                                selectedWantsList.clear()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xFF6482FF
                                )
                            ),
                            modifier = Modifier
                                .size(width = 130.dp, height = 60.dp)
                                .padding(6.dp)
                                .offset(x = (-8).dp)
                        )
                        {
                            Text(
                                text = "Clear Wants",
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(minSize = 60.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 0.dp, height = 190.dp)
                    ) {
                        items(count = wants.size) { item ->
                            val buttonColor = if (!wants[item].isSelected.value) {
                                Color(0xFFF1EDF2)
                            } else {
                                Color(0xFFe69500)
                            }
                            val textColor = if (!wants[item].isSelected.value) {
                                Gray
                            } else {
                                White
                            }
                            Button(
                                shape = RoundedCornerShape(24.dp),
                                onClick = {
                                    wants[item].isSelected.value = !(wants[item].isSelected.value)
                                    if (wants[item].isSelected.value) {
                                        doNotWants[item].isSelected.value = false
                                        selectedWantsList.add(wants[item])
                                        selectedWantsList.sortBy { it.title }
                                        selectedDoNotWantsList.remove(doNotWants[item])
                                    } else {
                                        selectedWantsList.remove(wants[item])
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = buttonColor
                                ),
                                modifier = Modifier
                                    .offset(x = 8.dp)
                                    .padding(6.dp)
                                    .size(width = 125.dp, height = 30.dp)
                            ) {
                                Text(
                                    text = wants[item].title,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                //Do Not Wants Selection
                Column() {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth())
                    {
                        Text(
                            text = "Ingredients I Don't Want: ",
                            color = Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = 8.dp),
                            fontSize = 20.sp
                        )
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                for (element in wants) {
                                    element.isSelected.value = false
                                }
                                selectedWantsList.clear()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xFF6482FF
                                )
                            ),
                            modifier = Modifier
                                .size(width = 130.dp, height = 60.dp)
                                .padding(6.dp)
                                .offset(x = (-8).dp)
                        )
                        {
                            Text(
                                text = "Clear Don't Wants",
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(minSize = 60.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 0.dp, height = 190.dp)
                    ) {
                        items(count = doNotWants.size) { item ->
                            val buttonColor = if (!doNotWants[item].isSelected.value) {
                                Color(0xFFF1EDF2)
                            } else {
                                Color(0xFFe69500)
                            }
                            val textColor = if (!doNotWants[item].isSelected.value) {
                                Gray
                            } else {
                                White
                            }
                            Button(
                                shape = RoundedCornerShape(24.dp),
                                onClick = {
                                    doNotWants[item].isSelected.value =
                                        !(doNotWants[item].isSelected.value)
                                    if (doNotWants[item].isSelected.value) {
                                        wants[item].isSelected.value = false
                                        selectedDoNotWantsList.add(doNotWants[item])
                                        selectedDoNotWantsList.sortBy { it.title }
                                        selectedWantsList.remove(wants[item])
                                    } else {
                                        selectedDoNotWantsList.remove(doNotWants[item])
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = buttonColor
                                ),
                                modifier = Modifier
                                    .offset(x = 8.dp)
                                    .padding(6.dp)
                                    .size(width = 125.dp, height = 30.dp)
                            ) {
                                Text(
                                    text = doNotWants[item].title,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                //Diets Selection
                Column() {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth())
                    {
                        Text(
                            text = "Diets:",
                            color = Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = 8.dp),
                            fontSize = 20.sp
                        )
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                for (element in diets) {
                                    element.ORisSelected.value = false
                                    element.ANDisSelected.value = false
                                }
                                selectedDietsList.clear()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xFF6482FF
                                )
                            ),
                            modifier = Modifier
                                .size(width = 130.dp, height = 60.dp)
                                .padding(6.dp)
                                .offset(x = (-8).dp)
                        )
                        {
                            Text(
                                text = "Clear Diets",
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(minSize = 60.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 0.dp, height = 190.dp)
                    ) {
                        items(count = diets.size) { item ->
                            val buttonColor = if (!diets[item].ORisSelected.value && !diets[item].ANDisSelected.value)
                                    Color(0xFFF1EDF2)
                                else if(diets[item].ORisSelected.value && !diets[item].ANDisSelected.value)
                                    Color(0xFF30B44E)
                                else
                                    Color(0xFFe69500)
                            val textColor = if (!diets[item].ORisSelected.value && !diets[item].ANDisSelected.value) {
                                Gray
                            } else {
                                White
                            }
                            Button(
                                shape = RoundedCornerShape(24.dp),
                                onClick = {
                                    if(!diets[item].ORisSelected.value && !diets[item].ANDisSelected.value) {
                                        diets[item].ORisSelected.value = true
                                        selectedDietsList.add(diets[item])
                                        selectedDietsList.sortBy { it.title }
                                    }
                                    else if (diets[item].ORisSelected.value && !diets[item].ANDisSelected.value) {
                                        val index = selectedDietsList.indexOf(dietsList.value[item])
                                        diets[item].ANDisSelected.value = true
                                        diets[item].ORisSelected.value = false
                                        selectedDietsList[index].ORisSelected.value = false
                                        selectedDietsList[index].ANDisSelected.value = true
                                    }
                                    else {
                                        selectedDietsList.remove(diets[item])
                                        diets[item].ANDisSelected.value = false
                                        diets[item].ORisSelected.value = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = buttonColor
                                ),
                                modifier = Modifier
                                    .offset(x = 8.dp)
                                    .padding(6.dp)
                                    .size(width = 125.dp, height = 30.dp)
                            ) {
                                Text(
                                    text = diets[item].title,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                //Intolerances Selection
                Column() {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth())
                    {
                        Text(
                            text = "Intolerances: ",
                            color = Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = 8.dp),
                            fontSize = 20.sp
                        )
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            onClick = {
                                for (element in intolerances) {
                                    element.isSelected.value = false
                                }
                                selectedIntolerancesList.clear()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xFF6482FF
                                )
                            ),
                            modifier = Modifier
                                .size(width = 130.dp, height = 60.dp)
                                .padding(6.dp)
                                .offset(x = (-8).dp)
                        )
                        {
                            Text(
                                text = "Clear Intolerances",
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(minSize = 60.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 0.dp, height = 190.dp)
                    ) {
                        items(count = intolerances.size) { item ->
                            val buttonColor = if (!intolerances[item].isSelected.value) {
                                Color(0xFFF1EDF2)
                            } else {
                                Color(0xFFe69500)
                            }
                            val textColor = if (!intolerances[item].isSelected.value) {
                                Gray
                            } else {
                                White
                            }
                            Button(
                                shape = RoundedCornerShape(24.dp),
                                onClick = {
                                    intolerances[item].isSelected.value =
                                        !(intolerances[item].isSelected.value)
                                    if (intolerances[item].isSelected.value) {
                                        selectedIntolerancesList.add(intolerances[item])
                                        selectedDietsList.sortBy { it.title }
                                    }
                                    else
                                        selectedIntolerancesList.remove(intolerances[item])
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = buttonColor
                                ),
                                modifier = Modifier
                                    .offset(x = 8.dp)
                                    .padding(6.dp)
                                    .size(width = 125.dp, height = 30.dp)
                            ) {
                                Text(
                                    text = intolerances[item].title,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    shape = RoundedCornerShape(24.dp),
                    onClick = {
                        suggestionListRequest.value = true
                        screenState.value = 4
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            0xFFe69500
                        )
                    ),
                    modifier = Modifier
                        .size(width = 150.dp, height = 60.dp)
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
    //Tailored Food Suggestion Screen
    if (state == 4) {
        if (suggestionListRequest.value) {
            val wantsBuilder = StringBuilder()
            val doNotWantsBuilder = StringBuilder()
            val dietBuilder = StringBuilder()
            val intolerancesBuilder = StringBuilder()
            val cuisineBuilder = StringBuilder()
            val typeBuilder = StringBuilder()
            if(quickSelectRequest.value){
                if(quickSelectedItem[0].isCuisine)
                    cuisineBuilder.append(quickSelectedItem[0].title)
                else
                    typeBuilder.append(quickSelectedItem[0].title)
            }
            for (want in wants) {
                if (want.isSelected.value)
                    wantsBuilder.append(want.title + ",")
            }
            for (doNotWant in doNotWants) {
                if (doNotWant.isSelected.value)
                    doNotWantsBuilder.append(doNotWant.title + ",")
            }
            for (diet in diets) {
                if(diet.ANDisSelected.value)
                    doNotWantsBuilder.append(diet.title + ",")
                if (diet.ORisSelected.value)
                    doNotWantsBuilder.append(diet.title + "|")
            }
            for (intolerance in intolerances) {
                if (intolerance.isSelected.value)
                    doNotWantsBuilder.append(intolerance.title + ",")
            }
            LaunchedEffect(key1 = suggestionListRequest) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                    val response = RetrofitInstance.spoonacularApi.getTailoredSuggestions(
                        includeNutrition = false,
                        addRecipeInformation = false,
                        wantsBuilder.toString(),
                        doNotWantsBuilder.toString(),
                        dietBuilder.toString(),
                        intolerancesBuilder.toString(),
                        instructionsRequired = true,
                        typeBuilder.toString(),
                        cuisineBuilder.toString(),
                        100,
                        "random",
                        "6a802448b4a34e4daaeb8be9bfb8d399"
                    )
                    recipeList = getTailoredResultsList(response)
                    if (recipeList?.isNotEmpty() == true) {
                        displayResponse = recipeList?.get(0)?.let {
                            RetrofitInstance.spoonacularApi.getRecipeInfo(
                                it.id, includeNutrition = false, "6a802448b4a34e4daaeb8be9bfb8d399"
                            )
                        }!!
                        displayTailoredRecipe(displayResponse)
                        recipeListPosition.value = 1
                    }
                    else
                        noRecipeResults.value = true
                }
            }
            suggestionListRequest.value = false
        }

        if (changeRequested.value) {
            LaunchedEffect(key1 = changeRequested.value) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                    val response = RetrofitInstance.spoonacularApi.getRecipeInfo(
                        recipeList!![recipePosition].id,
                        includeNutrition = false,
                        "6a802448b4a34e4daaeb8be9bfb8d399"
                    )
                    displayTailoredRecipe(response)
                    recipeListPosition.value += 1
                }
            }
            changeRequested.value = false
        }
        if(noRecipeResults.value){
            AlertDialog(
                onDismissRequest = { suggestionListRequest.value = false
                    screenState.value = 1
                    quickSelectedItem.clear()
                    quickSelectRequest.value = false
                    noRecipeResults.value = false},

                title = { Text(text = "No Results",
                    color = Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp) },

                text = { Text("Sorry, but your taste is a bit TOO refined! Please go " +
                        "back and change your search requirements.",
                    color = Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center) },

                confirmButton = {
                    Button(
                        shape = RoundedCornerShape(24.dp),
                        onClick = {
                            suggestionListRequest.value = false
                            screenState.value = 1
                            quickSelectedItem.clear()
                            quickSelectRequest.value = false
                            noRecipeResults.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                0xFFe69500
                            )
                        ),
                        modifier = Modifier
                            .size(width = 125.dp, height = 60.dp)

                    )
                    {
                        Text(
                            text = "Go Back",
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
        else{
            Column() {
                Text(
                    text = "<",
                    modifier = Modifier
                        .clickable { suggestionListRequest.value = false
                            screenState.value = 1
                            quickSelectedItem.clear()
                            quickSelectRequest.value = false
                            noRecipeResults.value = false }
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
                                if(recipePosition < recipeList?.size!!)
                                    changeRequested.value = true
                                else
                                    suggestionListRequest.value = true
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
fun displayRandomSuggestion(response: Response<RandomRecipesList>){
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
        else
            recipeImageUrl.value = "https://media.istockphoto.com/vectors/no-image-available-icon-vector-id1216251206?k=6&m=1216251206&s=612x612&w=0&h=G8kmMKxZlh7WyeYtlIHJDxP5XRGm9ZXyLprtVJKxd-o="
}

@RequiresApi(Build.VERSION_CODES.N)
fun getTailoredResultsList(response: Response<TailoredRecipesList>): List<Result>? {
    return response.body()?.results
}

@RequiresApi(Build.VERSION_CODES.N)
fun displayTailoredRecipe(response: Response<Recipe>) {
    val ingredientsBuilder = StringBuilder()
    val instructionsBuilder = StringBuilder()
    val responseBody = response.body()
    val newImage = responseBody?.image
    responseBody?.extendedIngredients?.forEach {
        ingredientsBuilder.append(
            it.amount.toString() + " " + it.unit + " "
                    + it.nameClean + "\n"
        )
    }

    responseBody?.analyzedInstructions?.forEach { it1 ->
        if (it1.steps.isNotEmpty()) {
            it1.steps.forEach {
                instructionsBuilder.append(it.number.toString() + ". " + it.step + "\n")
            }
        } else
            instructionsBuilder.append("")
    }
    if (responseBody != null) {
        titleText.value =
            (Html.fromHtml(responseBody.title, 0)
                .toString())
    }
    if (responseBody != null) {
        summaryText.value =
            (Html.fromHtml(responseBody.summary, 0)
                .toString())
    }
    instructionsText.value = instructionsBuilder.toString()
    ingredientsText.value = ingredientsBuilder.toString()
    if (newImage != null) {
        recipeImageUrl.value = newImage
    }
    else
        recipeImageUrl.value = "https://media.istockphoto.com/vectors/no-image-available-icon-vector-id1216251206?k=6&m=1216251206&s=612x612&w=0&h=G8kmMKxZlh7WyeYtlIHJDxP5XRGm9ZXyLprtVJKxd-o="
}

private fun generateIngredients(): List<PreferencesSelectionModel> {
    val ingredients = mutableListOf<PreferencesSelectionModel>()
    ingredients.add(PreferencesSelectionModel("Beef", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Alcohol", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Cheese", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Chicken", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Apple", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Pork", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Potatoes", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Cilantro", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Fish", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Tofu", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Rice", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Egg", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Peach", mutableStateOf(false)))
    ingredients.add(PreferencesSelectionModel("Venison", mutableStateOf(false)))

    return ingredients.toList().sortedBy { it.title }
}

private fun generateDiets(): List<DietsSelectionModel> {
    val diets = mutableListOf<DietsSelectionModel>()
    diets.add(DietsSelectionModel("Gluten Free"))
    diets.add(DietsSelectionModel("Ketogenic"))
    diets.add(DietsSelectionModel("Vegatarian"))
    diets.add(DietsSelectionModel("Lacto Vegetarian"))
    diets.add(DietsSelectionModel("Ovo Vegetarian"))
    diets.add(DietsSelectionModel("Vegan"))
    diets.add(DietsSelectionModel("Pescetarian"))
    diets.add(DietsSelectionModel("Paleo"))
    diets.add(DietsSelectionModel("Primal"))
    diets.add(DietsSelectionModel("Low FODMAP"))
    diets.add(DietsSelectionModel("Whole30"))

    return diets.toList().sortedBy { it.title }
}

private fun generateIntolerances(): List<PreferencesSelectionModel> {
    val intolerances = mutableListOf<PreferencesSelectionModel>()
    intolerances.add(PreferencesSelectionModel("Dairy"))
    intolerances.add(PreferencesSelectionModel("Egg"))
    intolerances.add(PreferencesSelectionModel("Gluten"))
    intolerances.add(PreferencesSelectionModel("Grain"))
    intolerances.add(PreferencesSelectionModel("Peanut"))
    intolerances.add(PreferencesSelectionModel("Seafood"))
    intolerances.add(PreferencesSelectionModel("Sesame"))
    intolerances.add(PreferencesSelectionModel("Shellfish"))
    intolerances.add(PreferencesSelectionModel("Soy"))
    intolerances.add(PreferencesSelectionModel("Sulfite"))
    intolerances.add(PreferencesSelectionModel("Tree Nut"))
    intolerances.add(PreferencesSelectionModel("Wheat"))

    return intolerances.toList().sortedBy { it.title }
}

private fun generateQuickSelection(): List<QuickSelectionModel> {
    val quickSelect = mutableListOf<QuickSelectionModel>()
    quickSelect.add(
        QuickSelectionModel(
            "Breakfast",
            R.drawable.breakfast,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Side Dish",
            R.drawable.side_dish,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Main Course",
            R.drawable.main_course,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Dessert",
            R.drawable.dessert,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Appetizer",
            R.drawable.appetizer,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Beverage",
            R.drawable.beverage,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Snack",
            R.drawable.snack,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Fingerfood",
            R.drawable.fingerfood,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Drink",
            R.drawable.drink,
            isType = true,
            isCuisine = false
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "American",
            R.drawable.american,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "African",
            R.drawable.bunny_chow,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Korean",
            R.drawable.kimchi_jjigae,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Chinese",
            R.drawable.chinese,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "French",
            R.drawable.ratatouille,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "European",
            R.drawable.european,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Eastern European",
            R.drawable.borscht,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "German",
            R.drawable.german,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Greek",
            R.drawable.greek,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Indian",
            R.drawable.chicken_tikka_masala,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Italian",
            R.drawable.italian,
            isType = false,
            isCuisine = true
        )
    )
    quickSelect.add(
        QuickSelectionModel(
            "Japanese",
            R.drawable.ochazuke,
            isType = false,
            isCuisine = true
        )
    )
    return quickSelect.toList().sortedBy { it.title }

}
