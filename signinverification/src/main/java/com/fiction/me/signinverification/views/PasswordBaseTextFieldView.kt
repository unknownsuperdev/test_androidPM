package com.fiction.me.signinverification.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiction.me.Constants.Companion.PASSWORD_CHARS_MAX_LENGTH
import com.fiction.me.baseui.theme.*
import com.fiction.me.signinverification.HelperStatusType
import com.fiction.me.signinverification.R
import com.fiction.me.theme.MyTypography
import com.fiction.me.theme.Shape
import com.fiction.me.theme.robotoFontFamily

@Composable
fun PasswordBaseTextFieldView(
    hintPasswordExpanded: String,
    hintPasswordCollapsed: String,
    password: MutableState<String>,
    helperStatusType: MutableState<HelperStatusType>,
    helperText: MutableState<String>,
    isStatusNotNone: MutableState<Boolean>,
    onPasswordValidListener: (password: String, isFocused: Boolean) -> Unit
) {
    val borderColor = remember { mutableStateOf(Black300) }
    val backgroundColor = remember { mutableStateOf(Black300) }
    val isFocused = remember { mutableStateOf(false) }
    val hint = remember { mutableStateOf(hintPasswordExpanded) }
    val passwordVisibility = remember { mutableStateOf(false) }

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(
                    width = 1.5.dp,
                    color = borderColor.value,
                    shape = RoundedCornerShape(12.dp)
                )
                .onFocusChanged {
                    isFocused.value = it.isFocused
                    onPasswordValidListener(password.value, it.isFocused)
                    if (it.isFocused) isStatusNotNone.value = true
                    hint.value = if (it.isFocused) hintPasswordCollapsed else hintPasswordExpanded
                },
            value = password.value,
            onValueChange = { newText ->
                if (newText.length < PASSWORD_CHARS_MAX_LENGTH)
                    password.value = newText
            },
            textStyle = MyTypography.body1,
            shape = Shape.medium,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = backgroundColor.value,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (password.value.isNotEmpty()) {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisibility.value)
                                    R.drawable.ic_pass_eye_closed
                                else R.drawable.ic_pass_eye_opened
                            ),
                            contentDescription = "Password Icon", tint = PrimaryWhite
                        )
                    }
                }
            },
            label = if (!(password.value.isNotEmpty() && !isFocused.value)) {
                {
                    Text(
                        text = hint.value,
                        style = TextStyle(
                            color = Gray,
                            fontSize = if (isFocused.value) 9.sp else 15.sp,
                            fontFamily = robotoFontFamily,
                            fontWeight = if (isFocused.value) FontWeight.Medium else FontWeight.Normal
                        )
                    )
                }
            } else {
                null
            },
        )
        if (helperText.value.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = helperText.value,
                style = TextStyle(
                    color = borderColor.value,
                    fontSize = 11.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
    when (helperStatusType.value) {
        HelperStatusType.ACTIVE -> {
            borderColor.value = PrimaryWhite
            backgroundColor.value = Black300
        }

        HelperStatusType.SUCCESS -> {
            borderColor.value = ColorDarkSuccess500
            backgroundColor.value = ColorDarkSuccess100
        }

        HelperStatusType.WARNING -> {
            borderColor.value = Orange
            backgroundColor.value = ColorDarkWarning100
        }

        HelperStatusType.ERROR -> {
            borderColor.value = Red500
            backgroundColor.value = ColorDarkError100
        }

        else -> {
            borderColor.value = Black300
            backgroundColor.value = Black300
        }
    }
}
