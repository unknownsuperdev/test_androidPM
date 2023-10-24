package com.fiction.me.ui.fragments.loginregistration.earncoincompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fiction.me.R
import com.fiction.me.baseui.theme.*
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.BaseTextStyle
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.CloseDialog
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.DialogButton

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun EarnedCoinDialog(
    state: MutableState<Boolean>,
    onDismiss: () -> Unit
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
            ContentDialog(onDismiss)
        }
    }

}

@Composable
//@Preview(showSystemUi = true)
fun ContentDialog(
    onDismiss: () -> Unit,
) {
    Surface(
        elevation = 5.dp,
        shape = RoundedCornerShape(4.dp),
        color = Black100,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(24.dp)
            .border(2.dp, color = Black400, shape = RoundedCornerShape(15.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CloseDialog(onDismiss)
            Image(
                painterResource(id = R.drawable.ic_earn_coins),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            BaseTextStyle(R.string.you_earned_free_coins, PrimaryWhite, 17)
            BaseTextStyle(R.string.will_automatically_be_unlocked, Gray, 15)
            DialogButton(onDismiss, R.string.check_balance)
        }
    }
}