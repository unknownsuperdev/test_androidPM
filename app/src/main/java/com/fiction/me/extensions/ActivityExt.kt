package com.fiction.me.extensions

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.view.WindowManager

fun Activity.shareBookWithIntent(message: String) {
    val intent = Intent()
    val sendIntent: Intent = intent.apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    startActivity(createChooser(sendIntent, null))
}

fun Activity.preventScreenshot(enable: Boolean) {
    if (enable){
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }else{
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}
