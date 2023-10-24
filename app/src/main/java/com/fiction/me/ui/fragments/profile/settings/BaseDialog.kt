package com.fiction.me.ui.fragments.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fiction.me.R
import com.fiction.me.baseui.theme.Black400
import com.fiction.me.baseui.theme.LavenderGrey
import com.fiction.me.baseui.theme.SecondaryPurpleDark600
import com.fiction.me.theme.MyTypography
import com.fiction.me.theme.robotoFontFamily

@Composable
fun BaseDialog(
    state: MutableState<Boolean>,
    onDismiss: (isConfirm: Boolean) -> Unit,
) {

    if (state.value) {
        Dialog(
            onDismissRequest = {
                onDismiss(false)
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            ContentDialog(onDismiss)
        }
    }

}

@Composable
//@Preview(showSystemUi = true)
fun ContentDialog(
    onDismiss: (isConfirm: Boolean) -> Unit,
) {
    Surface(
        elevation = 5.dp,
        shape = RoundedCornerShape(8.dp),
        color = Black400,
        modifier = Modifier
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MyTypography.body1.copy(color = LavenderGrey),
                text = stringResource(id = R.string.sure_for_sign_out)
            )
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.End)
            ) {
                ActionTextButton(R.string.cancel, 24) {
                    onDismiss(false)
                }
                ActionTextButton(R.string.sign_out, 2) {
                    onDismiss(true)
                }
            }
        }
    }
}

@Composable
fun ActionTextButton(
    textRes: Int,
    paddingEnd: Int,
    onDismiss: () -> Unit
) {
    TextButton(
        modifier = Modifier.padding(end = paddingEnd.dp, top = 22.dp),
        onClick = {
            onDismiss()
        }
    ) {
        Text(
            text = stringResource(id = textRes),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium
            ),
            color = SecondaryPurpleDark600
        )
    }
}