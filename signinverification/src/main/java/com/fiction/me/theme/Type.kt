package com.fiction.me.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.fiction.me.baseui.theme.Gray
import com.fiction.me.baseui.theme.PrimaryWhite
import com.fiction.me.signinverification.R

val robotoFontFamily = FontFamily(
    Font(R.font.roboto_thin_100, FontWeight.Thin),
    Font(R.font.roboto_light_300, FontWeight.Light),
    Font(R.font.roboto_regular_400, FontWeight.Normal),
    Font(R.font.roboto_medium_500, FontWeight.Medium),
    Font(R.font.roboto_bold_700, FontWeight.Bold),
    Font(R.font.roboto_black_900, FontWeight.Black),
)

val MyTypography = Typography(
    h1 = TextStyle(
        color = PrimaryWhite,
        fontSize = 26.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium
    ),
    h3 = TextStyle(
        color = PrimaryWhite,
        fontSize = 17.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium
    ),
    h4 = TextStyle(
        color = PrimaryWhite,
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium
    ),
    body1 = TextStyle(
        color = PrimaryWhite,
        fontSize = 15.sp,
        letterSpacing = 0.15.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal
    ),

    button =  TextStyle(
        color = PrimaryWhite,
        fontSize = 17.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium
    ),
    body2 = TextStyle(
        fontSize = 15.sp,
        color = Gray,
        letterSpacing = 0.15.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal
    )
    // similarly, override other parameters like h2, subtitle, etc...
)