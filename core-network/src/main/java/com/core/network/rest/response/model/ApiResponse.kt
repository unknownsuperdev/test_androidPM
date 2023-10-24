package com.core.network.rest.response.model

sealed class ApiResponse<R>

class ApiResponseSuccess<R>(val data: R) : ApiResponse<R>()

class ApiResponseError<R>(val code: Int, val errors: List<ErrorResponseData>, val header: String? = null) : ApiResponse<R>()

fun <T> ApiResponse<T>.process(
    onSuccess: ((T) -> Unit)? = null,
    onError: ((List<ErrorResponseData>) -> Unit)? = null
) {
    when (this) {
        is ApiResponseSuccess -> onSuccess?.invoke(this.data)
        is ApiResponseError -> onError?.invoke(this.errors)
    }
}