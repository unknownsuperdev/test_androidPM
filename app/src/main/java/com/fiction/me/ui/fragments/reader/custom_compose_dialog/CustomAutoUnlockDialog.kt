package com.fiction.me.ui.fragments.reader.custom_compose_dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fiction.me.R
import com.fiction.me.baseui.theme.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomAutoUnlockDialog(
    onDismiss: () -> Unit,
    onTurnSwitch: (isTurnOn: Boolean) -> Unit,
    isTurnOnSwitch: Boolean,
    state: MutableState<Boolean>
) {

    if (state.value) {
        Dialog(
            onDismissRequest = {
                onDismiss()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            ContentDialog(onDismiss, onTurnSwitch, isTurnOnSwitch)
        }
    }

}

@Composable
//@Preview(showSystemUi = true)
fun ContentDialog(
    onDismiss: () -> Unit,
    onTurnSwitch: (isTurnOn: Boolean) -> Unit,
    isTurnOnSwitch: Boolean
) {
    Surface(
        elevation = 5.dp,
        shape = RoundedCornerShape(4.dp),
        color = Black100,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .border(2.dp, color = Black400, shape = RoundedCornerShape(15.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CloseDialog(onDismiss)
            Switch(onTurnSwitch, isTurnOnSwitch)
            BaseTextStyle(R.string.auto_unlock_enabled, PrimaryWhite, 17)
            BaseTextStyle(R.string.will_automatically_be_unlocked, Gray, 15)

            DialogButton(onDismiss,R.string.okay)
            /*Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = SecondaryPurple500WithoutAlpha),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),

                ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(id = R.string.okay),
                    style = TextStyle(
                        color = PrimaryWhite,
                        fontSize = 17.sp
                    )
                )
            }*/
        }
    }
}

@Composable
fun Switch(
    onTurnSwitch: (isTurnOn: Boolean) -> Unit,
    isTurnOnSwitch: Boolean
) {
    val mCheckedState = remember { mutableStateOf(isTurnOnSwitch) }
    Switch(
        modifier = Modifier
            .scale(scale = 1.5f)
            .padding(bottom = 16.dp),
        checked = mCheckedState.value,
        onCheckedChange = {
            onTurnSwitch(it)
            mCheckedState.value = it
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
fun BaseTextStyle(
    textResourceId: Int,
    textColor: Color,
    textSize: Int
) {
    Text(
        modifier = Modifier
            .padding(bottom = 8.dp),
        text = stringResource(id = textResourceId),
        style = TextStyle(
            color = textColor,
            fontSize = textSize.sp
        ),
        textAlign = TextAlign.Center
    )
}