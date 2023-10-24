package com.fiction.me.ui.fragments.loginregistration.registrationproviders

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface GoogleSignInProvider {
    fun signIn(context: Context, resultListener: (String) -> Unit)
    fun setResultLauncher(resultLauncher: ActivityResultLauncher<Intent>)
    fun handleSignInResult(result: Intent?)
}