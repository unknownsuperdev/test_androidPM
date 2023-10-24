package com.fiction.me.ui.fragments.loginregistration.registrationproviders

import androidx.fragment.app.Fragment

interface FacebookSignInProvider {
    fun signIn(context: Fragment, resultListener: (String, Boolean) -> Unit)
    fun signOut()
}