package com.fiction.me.signinverification.views

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fiction.me.baseui.theme.Black300
import com.fiction.me.baseui.theme.Black600
import com.fiction.me.baseui.theme.PrimaryWhite
import com.fiction.me.signinverification.R
import com.fiction.me.theme.MyTypography
import com.fiction.me.theme.Shape

@SuppressLint("RememberReturnType")
@Composable
fun MainButton(
    isEnable: MutableState<Boolean> = mutableStateOf(true),
    text: String = stringResource(id = R.string.confirm),
    isShowProgressBar: MutableState<Boolean> = mutableStateOf(false),
    bottomPadding: MutableState<Int> = mutableStateOf(16),
    onClickAction: () -> Unit
) {
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(90, 41, 211),
            Color(169, 55, 223),
        )
    )
    val disableButtonBrush = Brush.horizontalGradient(
        colors = listOf(Black300, Black300)
    )
    Button(
        onClick = {
            if (isEnable.value) onClickAction()
           },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = bottomPadding.value.dp, top = 16.dp),

        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = if (isEnable.value) horizontalGradientBrush else disableButtonBrush,
                    shape = Shape.medium
                )
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!isShowProgressBar.value) {
                Text(
                    text = text,
                    color = if (isEnable.value) PrimaryWhite else Black600,
                    style = MyTypography.button
                )
            }
            var rotationAngle by remember { mutableStateOf(0f) }
            val rotation = remember {
                // Define the animation
                Animatable(0f)
            }
            LaunchedEffect(Unit) {
                // Animate the rotation
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
            rotationAngle = rotation.value
            if (isShowProgressBar.value) {
                Icon(
                    modifier = Modifier
                        .rotate(rotationAngle),
                    painter = painterResource(id = R.drawable.ic_rotate),
                    contentDescription = "Person Icon", tint = PrimaryWhite,
                )
            }
        }
    }
}