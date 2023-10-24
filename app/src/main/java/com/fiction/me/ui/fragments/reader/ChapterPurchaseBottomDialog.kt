package com.fiction.me.ui.fragments.reader

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiction.me.R
import com.fiction.me.baseui.theme.*

import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChapterPurchaseBottomDialog(
    myBalance: Int,
    isTurnOnUnlock: Boolean,
    state: MutableState<Boolean>,
    chapterPrice: Int,
    showDialogEvent: () -> Unit,
    unlockBtnClickListener: () -> Unit,
    onSwitchStateChangeListener: (isTurnOn: Boolean) -> Unit,
    onDismiss: () -> Unit,
    mCheckedState: MutableState<Boolean>,
    isShowProgressBar: MutableState<Boolean> = mutableStateOf(false)
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            if (it == ModalBottomSheetValue.Hidden)
                isShowProgressBar.value = false

            onDismiss()
            true
        }
    )

    val scope = rememberCoroutineScope()
    val isDismissDialog: (Boolean) -> Unit = {
        onDismiss()
    }
    LaunchedEffect(state.value) {
        if (state.value) {
            showDialogEvent()
        }
    }
    if (state.value) {
        scope.launch {
            sheetState.show()
        }
    } else scope.launch {
        sheetState.hide()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            BottomSheetContent(
                chapterPrice,
                unlockBtnClickListener,
                onSwitchStateChangeListener,
                myBalance,
                isTurnOnUnlock,
                isDismissDialog,
                mCheckedState,
                isShowProgressBar
            )
        },
    ) {
        //Rest of the Scaffold
    }
}

@Composable
//@Preview(showSystemUi = true)
fun BottomSheetContent(
    chapterPrice: Int,
    unlockBtnClickListener: () -> Unit,
    onSwitchStateChangeListener: (isTurnOn: Boolean) -> Unit,
    myBalance: Int,
    isTurnOnUnlock: Boolean,
    onDismiss: (Boolean) -> Unit,
    mCheckedState: MutableState<Boolean>,
    isShowProgressBar: MutableState<Boolean>,
) {

    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(90, 41, 211),
            Color(169, 55, 223),
        )
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Surface(
        modifier = Modifier.height(454.dp),
        color = Black100
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            IconButton(modifier = Modifier.then(
                Modifier
                    .size(48.dp)
                    .align(Alignment.End)
            ),
                onClick = {
                    onDismiss(true)
                }) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    "contentDescription",
                    tint = Color.White
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Black300,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BaseTextStyle(
                            R.string.chapter_price,
                            PrimaryWhite
                        )
                        CoinBalanceRow(chapterPrice)
                    }

                    Button(
                        onClick = {
                            isShowProgressBar.value = true
                             unlockBtnClickListener()
                            //    onDismiss(true)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),

                        ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(horizontalGradientBrush)
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Log.d("PROGRESS","acpvfffg")

                            if (!isShowProgressBar.value) {
                                Log.d("PROGRESS",isShowProgressBar.value.toString())

                                Text(
                                    // modifier = Modifier.padding(vertical = 8.dp),
                                    text = stringResource(id = R.string.unlock_chapter),
                                    style = TextStyle(
                                        color = PrimaryWhite,
                                        fontSize = 17.sp
                                    )
                                )
                            } else  {
                                Log.d("PROGRESS",isShowProgressBar.value.toString())

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
            }
            var visibleExplanationState by remember { mutableStateOf(false) }
            ExplanationText(isVisibleItem = visibleExplanationState)
            if (!visibleExplanationState) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BaseTextStyle(R.string.your_balance_, Gray)
                    CoinBalanceRow(myBalance)
                }


                Divider(
                    modifier = Modifier
                        .padding(top = 12.dp, start = 16.dp, end = 24.dp),
                    thickness = 2.dp,
                    color = Black300
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = stringResource(id = R.string.auto_unlock_),
                        style = TextStyle(
                            color = Gray,
                            fontSize = 15.sp
                        )
                    )

                    IconButton(
                        onClick = {
                            visibleExplanationState = !visibleExplanationState
                        }) {
                        Icon(
                            painterResource(id = R.drawable.ic_explanation),
                            "contentDescription",
                            tint = Gray
                        )
                    }
                }

                Switch(isTurnOnUnlock, onSwitchStateChangeListener, mCheckedState)
            }

        }
    }

}

@Composable
fun Switch(
    isChecked: Boolean,
    onSwitchStateChangeListener: (isTurnOn: Boolean) -> Unit,
    mCheckedState: MutableState<Boolean>
) {
    Switch(
        checked = mCheckedState.value,
        onCheckedChange = {
            mCheckedState.value = it
            onSwitchStateChangeListener(it)
        },

        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = SecondaryPurple500,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Black500,
        )
    )
}

@Composable
fun CoinBalanceRow(
    coinCounts: Int,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = coinCounts.toString(),
            style = TextStyle(
                color = PrimaryWhite,
                fontSize = 17.sp
            )
        )

        Image(
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
        )
    }
}

@Composable
fun ExplanationText(
    isVisibleItem: Boolean
) {
    if (isVisibleItem) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = Black400,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.explanation_content),
                style = TextStyle(
                    color = PrimaryWhite,
                    fontSize = 13.sp
                )
            )
        }
    }
}


@Composable
fun TextWithIcon() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = stringResource(id = R.string.auto_unlock_),
            style = TextStyle(
                color = Gray,
                fontSize = 15.sp
            )
        )

        Image(
            painter = painterResource(id = R.drawable.ic_explanation),
            contentDescription = null,
        )
        IconButton(
            onClick = {
                //visibleExplanationState = !visibleExplanationState
            }) {
            Icon(
                painterResource(id = R.drawable.ic_explanation),
                "contentDescription",
                tint = Gray
            )
        }
    }
}

@Composable
fun BaseTextStyle(
    textResourceId: Int,
    textColor: Color
) {
    Text(
        text = stringResource(id = textResourceId),
        style = TextStyle(
            color = textColor,
            fontSize = 15.sp
        )
    )
}