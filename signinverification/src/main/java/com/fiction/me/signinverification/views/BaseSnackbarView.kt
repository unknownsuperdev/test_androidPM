package com.fiction.me.signinverification.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiction.me.baseui.theme.Black300
import com.fiction.me.baseui.theme.ColorDarkSuccess500
import com.fiction.me.baseui.theme.PrimaryWhite
import com.fiction.me.baseui.theme.Red500
import com.fiction.me.baseui.theme.SecondaryPurpleDark600
import com.fiction.me.signinverification.R
import com.fiction.me.theme.Shape
import com.fiction.me.theme.robotoFontFamily


@ExperimentalMaterialApi
@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    icDrawable: MutableState<Int>,
    onDismiss: () -> Unit?
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(vertical = 8.dp),
                backgroundColor = Black300,
                shape = Shape.medium,
                content = {
                    Box(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(24.dp),
                                painter = painterResource(icDrawable.value),
                                contentDescription = "print",
                                tint = if (icDrawable.value == R.drawable.ic_success) ColorDarkSuccess500 else Red500
                            )
                            Text(
                                modifier = Modifier.padding(top = 0.dp),
                                text = data.message,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = robotoFontFamily,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = PrimaryWhite
                            )
                        }
                    }
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(
                            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            Text(
                                text = actionLabel,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = robotoFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = SecondaryPurpleDark600
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier
    )
}