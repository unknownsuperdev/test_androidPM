package com.fiction.me.signinverification

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.domain.interactors.ForgotPasswordUseCase
import com.fiction.domain.model.registration.ForgotPassword
import com.fiction.me.Constants
import com.fiction.me.extention.isValidEmail
import com.fiction.me.extention.isValidPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    val emailState = mutableStateOf("")
    val isEnableButton = mutableStateOf(false)
    var email = ""
    var errorText = ""
    var fieldRequired = ""
    var enterCorrectEmail = ""
    val isEmailStatusNotNone = mutableStateOf(false)
    val progressAnimation = mutableStateOf(false)
    val emailHelperText = mutableStateOf("")
    val emailHelperStatusType = mutableStateOf(HelperStatusType.NONE)
    private val _forgotPassword = MutableSharedFlow<ForgotPassword>()
    val forgotPassword = _forgotPassword.asSharedFlow()

    fun sendEmailForGetCode() {
        viewModelScope.launch {
            progressAnimation.value = true
            when (val result = forgotPasswordUseCase(email = emailState.value)) {
                is ActionResult.Success -> {
                    val data = result.result.copy(email = emailState.value)
                    _forgotPassword.emit(data)
                    progressAnimation.value = false
                }
                is ActionResult.Error -> {
                    progressAnimation.value = false
                    val errorMessage = (result.errors as? CallException)?.errorMessage
                    errorMessage?.forEach {
                        errorText = it.substringAfter('+')
                        emailHelperStatusType.value = HelperStatusType.ERROR
                        emailHelperText.value = errorText
                    }
                }
            }
        }
    }
    fun getMailValidation(isFocus: Boolean) {
        if (isFocus) {
            emailHelperText.value = ""
            emailHelperStatusType.value = HelperStatusType.ACTIVE
        }
        else {
            emailHelperStatusType.value = when {
                email.isValidEmail() -> {
                    emailHelperText.value = ""
                    HelperStatusType.SUCCESS
                }
                email.isEmpty() && isEmailStatusNotNone.value -> {
                    emailHelperText.value = fieldRequired
                    HelperStatusType.WARNING
                }
                email.isNotEmpty() && !email.isValidEmail() -> {
                    emailHelperText.value = enterCorrectEmail
                    HelperStatusType.ERROR
                }
                else -> {
                    emailHelperText.value = ""
                    HelperStatusType.NONE
                }
            }
        }
    }
    fun getButtonState(){
        isEnableButton.value = emailState.value.isValidEmail()
    }
}