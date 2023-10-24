package com.fiction.me.signinverification

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.domain.interactors.CheckResetCodeUseCase
import com.fiction.domain.interactors.ForgotPasswordUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RecoveryEmailViewModel(
    private val checkResetCodeUseCase: CheckResetCodeUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            delay(10000)
            isResendCodeShown.value = true
        }
    }
    val isButtonEnable = mutableStateOf(false)
    val isResendCodeShown = mutableStateOf(false)
    var smsCodeNumber = mutableStateOf("")
    var isErrorStatus = mutableStateOf(false)
    val progressAnimation = mutableStateOf(false)
    private val _isCodeConfirmed = MutableSharedFlow<String?>()
    val isCodeConfirmed = _isCodeConfirmed.asSharedFlow()

    private val _isShowSnackbar = MutableSharedFlow<String>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()
    var isError = false
    var initCount = 0
    fun checkResetCode(email: String) {
        viewModelScope.launch {
            progressAnimation.value = true
            when (val result = checkResetCodeUseCase(smsCodeNumber.value, email)) {
                is ActionResult.Success -> {
                    _isCodeConfirmed.emit(result.result)
                    progressAnimation.value = false
                }
                is ActionResult.Error -> {
                    val errorMessage = (result.errors as? CallException)?.errorMessage
                    var errorText = ""
                    errorMessage?.forEach {
                        errorText = it.substringAfter('+')
                    }
                    isError = true
                    _isShowSnackbar.emit(errorText)
                    isErrorStatus.value = true
                    progressAnimation.value = false
                    isButtonEnable.value = false
                    isResendCodeShown.value = true
                }
            }
        }
    }

    fun sendEmailForGetCode(email: String) {
        viewModelScope.launch {
            when (val result = forgotPasswordUseCase(email = email)) {
                is ActionResult.Success -> {
                    result.result.message?.let {
                        isError = false
                        _isShowSnackbar.emit(it)
                    }
                    delay(10000)
                    isResendCodeShown.value = true
                }
                is ActionResult.Error -> {
                    progressAnimation.value = false
                    val errorMessage = (result.errors as? CallException)?.errorMessage
                    errorMessage?.forEach {
                        val errorText = it.substringAfter('+')
                        isError = true
                        _isShowSnackbar.emit(errorText)
                        delay(10000)
                        isResendCodeShown.value = true
                    }
                }
            }
        }
    }

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            if (initCount < 2) {
                isError = false
               _isShowSnackbar.emit(message)
                initCount++
            }
        }
    }

}