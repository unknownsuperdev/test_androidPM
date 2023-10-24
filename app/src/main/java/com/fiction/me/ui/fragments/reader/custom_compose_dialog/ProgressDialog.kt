package com.fiction.me.ui.fragments.reader.custom_compose_dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fiction.me.baseui.theme.Black100
import com.fiction.me.baseui.theme.Black300
import com.fiction.me.baseui.theme.SecondaryDarkPurple500


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProgressDialog(
    state: MutableState<Boolean> = mutableStateOf(true)
) {

    if (state.value) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Black300.copy(0.4f)
            ) {
                ContentDialog(
                    getVerticalPaddingProgressSurface(),
                    getHorizontalPaddingProgressSurface()
                )
            }
        }
    }
}


@Composable
fun ContentDialog(
    paddingVertical: Float,
    paddingHorizontal: Float
) {
    Surface(
        elevation = 5.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = paddingHorizontal.dp, vertical = paddingVertical.dp),
        color = Black100,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size = 24.dp)
                .padding(32.dp),
            color = SecondaryDarkPurple500,
            strokeWidth = 2.dp
        )
    }
}


@Composable
fun getHorizontalPaddingProgressSurface(): Float {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val spaceAroundPins = 88.dp
    return (screenWidth - spaceAroundPins) / 2.dp
}

@Composable
fun getVerticalPaddingProgressSurface(): Float {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenHeightDp.dp
    val spaceAroundPins = 88.dp
    return (screenWidth - spaceAroundPins) / 2.dp
}