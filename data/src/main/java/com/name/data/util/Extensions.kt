package com.name.data.util


import com.name.core.ActionResult
import com.name.core.CallException
import retrofit2.Response

suspend fun <R> makeApiCall(call: suspend () -> ActionResult<R>) = try {
    call()
} catch (e: Exception) {
    ActionResult.Error(e)
}

fun <Model> analyzeResponse(
    response: Response<Model>
): ActionResult<Model> {
    when {
        response.isSuccessful -> {
            val responseBody = response.body()
            responseBody?.let {
                return ActionResult.Success(it)
            } ?: return ActionResult.Error(CallException(response.code(), response.message()))
        }
        else -> {
            return ActionResult.Error(CallException(response.code(), response.message()))
        }
    }
}
