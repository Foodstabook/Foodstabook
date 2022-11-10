package com.example.foodstabook.model

import com.example.foodstabook.activity.DefaultColor
import androidx.compose.animation.core.*
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal data class ShimmerData(
    val colors: List<Color>,
    val progress: Float,
    val drawBorder: Boolean = false
)

@Immutable
data class Shimmer(
    val colors: List<Color> = listOf(
        DefaultColor.copy(alpha = .9f),
    ),
    val animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing
        ),
        RepeatMode.Reverse
    ),
    val drawBorder: Boolean = false
) {

    companion object {
        operator fun invoke(
            color: Color,
            animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
                tween(
                    durationMillis = 3000,
                    easing = FastOutSlowInEasing
                ),
                RepeatMode.Reverse
            ),
            drawBorder: Boolean = false
        ): Shimmer {
            return Shimmer(
                colors = listOf(
                    color.copy(alpha = .9f),
                ),
                animationSpec = animationSpec,
                drawBorder = drawBorder
            )
        }
    }
}