package com.fiction.me.ui.fragments.reader.custom_compose_dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fiction.me.baseui.theme.SecondaryPurple500WithoutAlpha
import com.fiction.me.theme.MyTypography
import com.fiction.me.theme.Shape

@Composable
fun DialogButton(
    onDismiss : () -> Unit,
    btnTextId: Int
){
    Button(
        onClick = {
            onDismiss()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = SecondaryPurple500WithoutAlpha),
        shape = Shape.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),

        ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = btnTextId),
            style = MyTypography.h3
        )
    }
}