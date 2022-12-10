package com.example.foodstabook.model

import android.view.MotionEvent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.foodstabook.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    ratingNum: Int,
):Int {
    var ratingState by remember {
        mutableStateOf(ratingNum)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 30.dp else 30.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_foreground),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFEF6C00) else Color(0xFFA2ADB1)
            )
        }
    }
    return ratingState
}

