package ru.tripster.core

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

data class CallException(
    val errorCode: Int,
    val errorMessage: String? = null,
    val errorBody: ErrorBody? = null,
) : Exception() {
    companion object {
        fun CombinedLoadStates.toCallException() = (((refresh as LoadState.Error).error) as? CallException)
    }
}