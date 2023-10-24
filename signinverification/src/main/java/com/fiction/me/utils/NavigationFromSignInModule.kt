package com.fiction.me.utils

import android.content.Context

interface NavigationFromSignInModule {
    fun navigateToDestination(context: Context, destinationId: Int)
}