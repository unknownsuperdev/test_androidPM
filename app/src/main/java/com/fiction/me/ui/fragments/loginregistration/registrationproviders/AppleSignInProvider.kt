package com.fiction.me.ui.fragments.loginregistration.registrationproviders

import android.content.Context

interface AppleSignInProvider {
    fun signIn(context: Context, resultListener: (token: String, isCanceled: Boolean) -> Unit)
}