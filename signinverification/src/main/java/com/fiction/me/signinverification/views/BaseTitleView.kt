package com.fiction.me.signinverification.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fiction.me.theme.MyTypography

@Composable
fun BaseTitle(titleResId: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        text = stringResource(id = titleResId),
        style = MyTypography.h1
    )
}
@Composable
fun BaseCenterTitle(titleResId: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        text = stringResource(id = titleResId),
        style = MyTypography.h1,
        textAlign = TextAlign.Center
    )
}