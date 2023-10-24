package com.fiction.me.ui.fragments.loginregistration.signin.collapsingtoolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiction.me.R
import kotlin.math.roundToInt
import androidx.compose.ui.util.lerp
import com.fiction.me.baseui.theme.PrimaryWhite
import com.fiction.me.theme.MyTypography


private val ContentPadding = 16.dp
private val Elevation = 4.dp

private val ExpandedTextSize = 26.dp
private val CollapsedTextSize = 17.dp

@Composable
fun CollapsingToolbar(
    progress: Float,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    val welcomeTextSize = with(LocalDensity.current) {
        lerp(CollapsedTextSize.toPx(), ExpandedTextSize.toPx(), progress).toSp()
    }

    Surface(
        color = MaterialTheme.colors.background,
        elevation = Elevation,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = ContentPadding)
                    .fillMaxSize()
            ) {
                CollapsingToolbarLayout(progress = progress) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 10.dp)
                            .clickable {
                                onBackPressed()
                            }
                    )
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Welcome Back",
                        style = MyTypography.h1.copy(fontSize = welcomeTextSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun CollapsingToolbarLayout(
    progress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 2)

        val placeables = measurables.map {
            it.measure(constraints)
        }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {

            val expandedHorizontalGuideline = (constraints.maxHeight * 0.4f).roundToInt()
            val collapsedHorizontalGuideline = (constraints.maxHeight * 0.4f).roundToInt()

            val backButton = placeables[0]
            val welcomeText = placeables[1]

            backButton.placeRelative(
                x = 0,
                y = 16,
            )
            welcomeText.placeRelative(
                x = lerp(
                    start = constraints.maxWidth / 2 - welcomeText.width / 2,
                    stop = 0,
                    fraction = progress
                ),
                y = lerp(
                    start = collapsedHorizontalGuideline - welcomeText.height / 2,
                    stop = expandedHorizontalGuideline,
                    fraction = progress
                )
            )
        }
    }
}

@Preview
@Composable
fun CollapsingToolbarCollapsedPreview() {
    CollapsingToolbar(
        progress = 0f,
        onBackPressed = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    )
}

