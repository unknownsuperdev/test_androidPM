package com.fiction.me.signinverification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.ResetPasswordUseCase
import com.fiction.me.extention.isValidPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    val isShowProgressBar = mutableStateOf(false)
    private val _isCodeConfirmed = MutableSharedFlow<Boolean>()
    val isCodeConfirmed = _isCodeConfirmed.asSharedFlow()

    val isEnabledConfirmBtn = mutableStateOf(false)
    val passwordHelperStatusType = mutableStateOf(HelperStatusType.NONE)
    var passwordHelperText = mutableStateOf("")
    val isPasswordStatusNotNone = mutableStateOf(false)
    var passwordState = mutableStateOf("")
    var password = ""

    val repeatPassHelperStatusType = mutableStateOf(HelperStatusType.NONE)
    var repeatPassHelperText = mutableStateOf("")
    val isRepeatPassStatusNotNone = mutableStateOf(false)
    var repeatPassState = mutableStateOf("")
    var repeatPassword = ""
    var fieldRequired = ""
    var identicalPass = ""
    var invalidPass = ""

    fun resetPassword(token: String) {
        viewModelScope.launch {
            isShowProgressBar.value = true
            when (val result = resetPasswordUseCase(repeatPassState.value, token)) {
                is ActionResult.Success -> {
                    _isCodeConfirmed.emit(true)
                    Log.i("ForgotPass", ": $result")
                    isShowProgressBar.value = false
                }

                is ActionResult.Error -> {
                    isShowProgressBar.value = false
                    _isCodeConfirmed.emit(false)
                }
            }
        }
    }

    fun getPasswordValidation(isFocus: Boolean) {
        if (isFocus) {
            passwordHelperText.value = ""
            passwordHelperStatusType.value = HelperStatusType.ACTIVE
        } else {
            passwordHelperStatusType.value = when {
                password.isValidPassword() -> {
                    passwordHelperText.value = ""
                    HelperStatusType.SUCCESS
                }

                password.isEmpty() && isPasswordStatusNotNone.value -> {
                    passwordHelperText.value = fieldRequired
                    HelperStatusType.WARNING
                }

                password.isNotEmpty() && !password.isValidPassword() -> {
                    passwordHelperText.value = invalidPass
                    HelperStatusType.ERROR
                }

                else -> {
                    passwordHelperText.value = ""
                    HelperStatusType.NONE
                }
            }
        }
    }
    fun getRepeatPasswordValidation(isFocus: Boolean) {
        if (isFocus) {
            repeatPassHelperText.value = ""
            repeatPassHelperStatusType.value = HelperStatusType.ACTIVE
        } else {
            repeatPassHelperStatusType.value = when {
                repeatPassword.isValidPassword() && repeatPassword == password -> {
                    repeatPassHelperText.value = ""
                    HelperStatusType.SUCCESS
                }

                repeatPassword.isEmpty() && isRepeatPassStatusNotNone.value -> {
                    repeatPassHelperText.value =
                        if (password.isEmpty()) fieldRequired else identicalPass
                    HelperStatusType.WARNING
                }

                repeatPassword.isNotEmpty() && (!repeatPassword.isValidPassword() || repeatPassword != password) -> {
                    repeatPassHelperText.value =
                        if (repeatPassword != password) identicalPass else invalidPass
                    HelperStatusType.ERROR
                }

                else -> {
                    repeatPassHelperText.value = ""
                    HelperStatusType.NONE
                }
            }
        }
    }
    fun getButtonState() {
        isEnabledConfirmBtn.value =
            passwordState.value.isValidPassword() && passwordState.value == repeatPassState.value
    }
}