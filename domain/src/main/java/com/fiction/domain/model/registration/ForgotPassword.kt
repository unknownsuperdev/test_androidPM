package com.fiction.domain.model.registration

import android.os.Parcelable
import com.fiction.entities.response.registration.forgotpassword.ForgotPasswordResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotPassword(
    val code: Int,
    val email: String,
    val message: String?
): Parcelable {


    companion object{
        fun from(forgotPasswordResponse: ForgotPasswordResponse): ForgotPassword =
            with(forgotPasswordResponse) {
                ForgotPassword(
                    code ?: 0,
                    email ?: "",
                    message ?: ""
                )
            }
    }
}
