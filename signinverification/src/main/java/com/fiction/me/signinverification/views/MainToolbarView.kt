package com.fiction.me.signinverification.views

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.fiction.me.baseui.theme.PrimaryWhite


@Composable
fun MainToolbar(title: String = "",drawableId: Int, onBackPressed: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = PrimaryWhite,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painterResource(id = drawableId),
                    "contentDescription",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun MainToolbar(drawableId: Int, onBackPressed: () -> Unit) {
    TopAppBar(
        title = { Text("") },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = PrimaryWhite,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painterResource(id = drawableId),
                    "contentDescription",
                    tint = Color.White
                )
            }
        }
    )
}