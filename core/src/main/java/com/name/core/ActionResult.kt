package com.name.core

sealed class ActionResult<out S> {
    data class Success<S>(val result: S) : ActionResult<S>()
    data class Error(val errors: Throwable) : ActionResult<Nothing>()
}