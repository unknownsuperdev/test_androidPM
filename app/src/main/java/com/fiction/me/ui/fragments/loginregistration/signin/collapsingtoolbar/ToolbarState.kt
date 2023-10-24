package com.fiction.me.ui.fragments.loginregistration.signin.collapsingtoolbar

import androidx.compose.runtime.Stable

@Stable
interface ToolbarState {
    val offset: Float
    val height: Float
    val progress: Float
    var scrollValue: Int
}