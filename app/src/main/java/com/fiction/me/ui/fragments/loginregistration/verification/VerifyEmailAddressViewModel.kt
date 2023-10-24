package com.fiction.me.ui.fragments.loginregistration.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.domain.interactors.ResendEmailUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class VerifyEmailAddressViewModel(
    private val resendEmailUseCase: ResendEmailUseCase
) : ViewModel() {

    private val _resendEmail = MutableSharedFlow<String?>()
    val resendEmail = _resendEmail.asSharedFlow()
    var isError = false
    var isResendEmail = true

    fun resendEmailForVerify() {
        viewModelScope.launch {
            if (isResendEmail) {
                isResendEmail = false
                when (val result = resendEmailUseCase()) {
                    is ActionResult.Success -> {
                        isError = false
                        _resendEmail.emit(result.result)
                    }
                    is ActionResult.Error -> {
                        val errorMessage = (result.errors as? CallException)?.errorMessage
                        errorMessage?.forEach {
                            val errorText = it.substringAfter('+')
                            isError = true
                            _resendEmail.emit(errorText)
                        }
                    }
                }
            }
        }
    }
}