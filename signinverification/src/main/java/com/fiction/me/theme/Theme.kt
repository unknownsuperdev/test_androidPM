package com.fiction.me.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fiction.me.baseui.theme.*

private val DarkColorPalette = darkColors(
    primary = PrimaryWhite,
    primaryVariant = Black100,
    surface =  Black100,
    secondary = Heliotrope,
    background = Black100
)

private val LightColorPalette = lightColors(
    primary = Color.Blue,
    primaryVariant =  Color.Blue,
    secondary =  Color.Blue

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)



@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
      //  shapes = Shapes,
        content = content
    )
}