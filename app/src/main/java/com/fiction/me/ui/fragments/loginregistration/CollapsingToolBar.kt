package com.fiction.me.ui.fragments.loginregistration

import android.util.TypedValue
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.transform.CircleCropTransformation
import com.fiction.me.extensions.dpToPx
import com.fiction.me.extensions.toDp
import com.fiction.me.signinverification.R
import kotlin.math.max
import kotlin.math.min

@Composable
fun FoodCategoryDetailsScreen() {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )
    //Surface(color = MaterialTheme.colors.background) {
        Column {
            Surface(elevation = 4.dp) {
                CategoryDetailsCollapsingToolbar(scrollOffset)
            }
            Spacer(modifier = Modifier.height(2.dp))
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(count = 4) { item ->
                  //  var text by remember { mutableStateOf(TextFieldValue("")) }
                    TextField(
                        value = "text",
                        onValueChange = { newText ->
                           // text = newText
                        }
                    )
                    /*FoodItemRow(
                        item = item,
                        iconTransformationBuilder = {
                            transformations(
                                CircleCropTransformation()
                            )
                        }
                    )*/
                }
            }
     //   }
    }
}

@Composable
private fun CategoryDetailsCollapsingToolbar(
    //category: FoodItem?,
    scrollOffset: Float,
) {
    val imageSize by animateDpAsState(targetValue = max(5.dp, 300.dp * scrollOffset))
    Row {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = Color.Black
            ),
            elevation = 4.dp
        ) {
            Text(
                modifier = Modifier.size(max(5.dp, imageSize)),
                text = "Welcome back"
            )
        }
        /* FoodItemDetails(
             item = category,
             expandedLines = (max(3f, scrollOffset * 6)).toInt(),
             modifier = Modifier
                 .padding(
                     end = 16.dp,
                     top = 16.dp,
                     bottom = 16.dp
                 )
                 .fillMaxWidth()
         )*/
     }
    }
/*
@Composable
fun MyCollapsingToolbarLayout() {
    val scrollState = rememberScrollState()

    // Calculate the toolbar title alpha based on scroll offset
    val toolbarTitleAlpha = 1f - (scrollState.value / 200f).coerceIn(0f, 1f)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val toolbarHeight = with(LocalDensity.current) {
            56.dp
        }

        Column(Modifier.verticalScroll(scrollState)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(toolbarHeight)
                    .background(Color.Blue)
            ) {
                // Collapsing Toolbar contents
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp)
                ) {
                    Text(
                        text = "Title",
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = Color.White.copy(alpha = toolbarTitleAlpha),
                            textAlign = TextAlign.Start
                        ),
                       // modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

            }
            // Rest of the content
            Column(Modifier.padding(16.dp)) {
                // Content views below the toolbar
                // ...
            }
        }
    }
}*/
