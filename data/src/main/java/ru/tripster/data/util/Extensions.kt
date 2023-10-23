package ru.tripster.data.util


import android.util.Log
import com.google.gson.Gson
import retrofit2.Response
import ru.tripster.core.*
import ru.tripster.domain.Constants.Companion.AUTH_FAILED
import ru.tripster.domain.Constants.Companion.AUTH_NOT_ACTIVE

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
            if (response.code() == 204) return ActionResult.Success(true as Model) else {
                responseBody?.let {
                    return ActionResult.Success(it)
                } ?: return ActionResult.Error(CallException(response.code(), response.message()))
            }
        }
        else -> {
            val errorDetail =
                Gson().fromJson(response.errorBody()?.string(), ErrorDetail::class.java)

            val phoneError = Gson().fromJson(response.errorBody()?.string(), PhoneErrorBody::class.java)

            return when {
                phoneError != null -> ActionResult.Error(
                    CallException(
                        response.code(),
                        errorMessage = phoneError.phone[0]
                    )
                )

                !errorDetail.detail.isNullOrEmpty() ->
                    ActionResult.Error(
                        CallException(
                            response.code(),
                            errorMessage = errorDetail.detail
                        )
                    )

                else -> ActionResult.Error(CallException(response.code(), response.message()))
            }
        }
    }
}

fun <Model> analyzeAuthResponse(
    response: Response<Model>
): ActionResult<Model> {
    when {
        response.isSuccessful -> {
            val responseBody = response.body()
            if (response.code() == 204) return ActionResult.Success(true as Model) else {
                responseBody?.let {
                    return ActionResult.Success(it)
                } ?: return ActionResult.Error(CallException(response.code(), response.message()))
            }
        }
        else -> {
            val authErrorDetail =
                Gson().fromJson(response.errorBody()?.string(), AuthErrorBody::class.java)

            return when {
                authErrorDetail != null -> {

                    val code = if (authErrorDetail.code == "not_active")
                        AUTH_NOT_ACTIVE
                    else
                        AUTH_FAILED

                    val message = authErrorDetail.message.orEmpty()

                    ActionResult.Error(
                        CallException(
                            code,
                            message
                        )
                    )
                }
                else -> ActionResult.Error(CallException(response.code(), response.message()))
            }
        }
    }
}

fun <Model> analyzeConfirmEditResponse(
    response: Response<Model>
): ActionResult<Model> {
    when {
        response.isSuccessful -> {
            val responseBody = response.body()
            if (response.code() == 204) return ActionResult.Success(true as Model) else {
                responseBody?.let {
                    return ActionResult.Success(it)
                } ?: return ActionResult.Error(CallException(response.code(), response.message()))
            }
        }
        else -> {
            val phoneErrorDetail =
                Gson().fromJson(response.errorBody()?.string(), PhoneErrorBody::class.java)

            return when {
                phoneErrorDetail != null -> {
                    val message = phoneErrorDetail.phone[0]
                    ActionResult.Error(
                        CallException(
                            response.code(),
                            message
                        )
                    )
                }
                else -> ActionResult.Error(CallException(response.code(), response.message()))
            }
        }
    }
}