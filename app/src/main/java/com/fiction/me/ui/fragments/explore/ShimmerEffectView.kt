package com.fiction.me.ui.fragments.explore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiction.me.extensions.shimmerBackground

@Composable
fun ShimmerEffect() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, top = 34.dp)
    ) {
        ShimmerItem(
            298,
            2
        )
        repeat(2) {
            ShimmerItem(
                120,
                3
            )
        }
    }
}

@Composable
fun ShimmerItem(
    width: Int,
    repeatCount: Int
) {
    Spacer(
        modifier = Modifier
            .padding(bottom = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(22.dp)
            .clip(RoundedCornerShape(4.dp))
            .shimmerBackground()
    )

    Row(
        modifier = Modifier
            .padding(start = 1.dp, bottom = 16.dp)
    ) {
        repeat(repeatCount) {
            Spacer(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(width.dp)
                    .height(164.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerBackground()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShimmerPreview() {
    ShimmerEffect()
}

