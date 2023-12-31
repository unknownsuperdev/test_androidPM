package com.fiction.me.ui.fragments.reader.custom_compose_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fiction.me.R

@Composable
fun CloseDialog(
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(modifier = Modifier.then(
            Modifier
                .size(24.dp)
        ),
            onClick = { onDismiss() }) {
            Icon(
                painterResource(id = R.drawable.ic_close),
                "contentDescription",
                tint = Color.White
            )
        }
    }
}
