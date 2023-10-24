package com.fiction.me.signinverification.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fiction.me.theme.MyTypography


@Composable
fun DescriptionText(textResId: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        text = stringResource(id = textResId),
        style = MyTypography.body1
    )
}

@Composable
fun DescriptionText(text: AnnotatedString, textColor: Color) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        text = text,
        style = MyTypography.body2,
        color = textColor,
    )
}

@Composable
fun DescriptionCenterText(text: String, style: TextStyle) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        text = text,
        style = style,
        textAlign = TextAlign.Center
    )
}