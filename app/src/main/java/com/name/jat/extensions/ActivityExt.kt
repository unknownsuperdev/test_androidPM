package com.name.jat.extensions

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser

fun Activity.shareBookWithIntent(message: String) {
    val intent = Intent()
    val sendIntent: Intent = intent.apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    startActivity(createChooser(sendIntent, null))
}
