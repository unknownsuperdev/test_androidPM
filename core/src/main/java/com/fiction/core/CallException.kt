package com.fiction.core

data class CallException(
    val errorCode: Int,
    val errorMessage: List<String>? = null,
    val errorBody: ErrorBody? = null
) : Exception()