package com.fiction.me.extensions

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.fiction.me.baseui.theme.Black100
import com.fiction.me.baseui.theme.Black300

fun Modifier.shimmerBackground(): Modifier = composed {
    val gradient = listOf(
        Black300,
        Black100,
        Black100,
        Black300
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500, // duration for the animation
                easing = FastOutLinearInEasing,
            )
        )
    )

    val brush = Brush.linearGradient(
        colors = gradient,
        start = Offset(-30f, -30f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    return@composed this.then(background(brush))
}


