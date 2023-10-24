package com.fiction.me.extention

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}
fun NavController.safeNavigate(actionId: Int, params: Bundle) {
    currentDestination?.getAction(actionId)?.run { navigate(actionId, params) }
}