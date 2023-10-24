package com.fiction.me.ui.fragments.loginregistration

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.fiction.me.baseui.theme.SecondaryPurpleDark600
import com.fiction.me.theme.MyTypography


@Composable
fun ClickableTextView(
    clickableText: String,
    defaultText: String,
    paddingTop: Int = 32,
    onClickListener: () -> Unit
){
    val annotatedClickableString = buildAnnotatedString {
        append(defaultText)
        withStyle(style = SpanStyle(color = SecondaryPurpleDark600)) {
            pushStringAnnotation(tag = clickableText, annotation = clickableText)
            append(" $clickableText")
        }
    }
    ClickableText(
        text = annotatedClickableString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = paddingTop.dp, bottom = 8.dp),
        style = MyTypography.h4,
        onClick = { offset ->
            annotatedClickableString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { span ->
                    Log.i("ClickableText", "Clicked on ${span.item}")
                    onClickListener()
                }
        })
}