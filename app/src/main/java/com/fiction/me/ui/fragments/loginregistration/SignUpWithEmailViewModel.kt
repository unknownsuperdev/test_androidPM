package com.fiction.me.ui.fragments.loginregistration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.domain.interactors.ClearAllCashedServerInfoUseCase
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.RegisterTokenUseCase
import com.fiction.domain.interactors.SetAuthTypeUseCase
import com.fiction.domain.model.enums.AuthType
import com.fiction.me.extention.isValidEmail
import com.fiction.me.extention.isValidPassword
import com.fiction.me.signinverification.HelperStatusType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignUpWithEmailViewModel(
    private val registerTokenUseCase: RegisterTokenUseCase,
    private val setAuthTypeUseCase: SetAuthTypeUseCase,
    private val clearAllCashedServerInfoUseCase: ClearAllCashedServerInfoUseCase,
    private val getExploreDataUseCase: ExploreDataUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase
) : ViewModel() {

    private val _signedUp = MutableSharedFlow<Unit>()
    val signedUp = _signedUp.asSharedFlow()

    var isShowProgressBar = mutableStateOf(false)
    val isEnabledConfirmBtn = mutableStateOf(false)
    val emailHelperStatusType = mutableStateOf(HelperStatusType.NONE)
    var emailHelperText = mutableStateOf("")
    val isEmailStatusNotNone = mutableStateOf(false)
    val passwordHelperStatusType = mutableStateOf(HelperStatusType.NONE)
    var passwordHelperText = mutableStateOf("")
    val isPasswordStatusNotNone = mutableStateOf(false)
    var passwordState = mutableStateOf("")
    var emailState = mutableStateOf("")
    var email = ""
    var password = ""
    var errorText = ""
    var fieldRequired = ""
    var enterCorrectEmail = ""
    var invalidPass = ""
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
    fun getPasswordValidation(isFocus: Boolean) {
        if (isFocus) {
            passwordHelperText.value = ""
            passwordHelperStatusType.value = HelperStatusType.ACTIVE
        }
        else {
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

    fun getButtonState(){
        isEnabledConfirmBtn.value = emailState.value.isValidEmail() && passwordState.value.isValidPassword()
    }
    fun signUp() {
        isShowProgressBar.value = true
        viewModelScope.launch {
            when (val result = registerTokenUseCase(email, password)) {
                is ActionResult.Success -> {
                    clearAllCashedServerInfoUseCase()
                    val asyncCalls = listOf(
                        async { getCurrentReadingBooksUseCase(isMakeCallAnyway = true) },
                        async { getExploreDataUseCase() },
                        async { getProfileInfoUseCase(isMakeCallAnyWay = true) }
                    )
                    asyncCalls.awaitAll()
                    setAuthTypeUseCase(AuthType.MAIL)
                    _signedUp.emit(Unit)
                    isShowProgressBar.value = false
                }
                is ActionResult.Error -> {
                    val errorMessage = (result.errors as? CallException)?.errorMessage
                    errorMessage?.forEach {
                        errorText = it.substringAfter('+')
                    }
                    emailHelperStatusType.value = HelperStatusType.ERROR
                    passwordHelperStatusType.value = HelperStatusType.ERROR
                    passwordHelperText.value = errorText
                    isShowProgressBar.value = false
                }
            }
        }
    }
    fun clearStates() {
        isShowProgressBar.value = false
        isEnabledConfirmBtn.value = false
        emailHelperStatusType.value = HelperStatusType.NONE
        emailHelperText.value = ""
        isEmailStatusNotNone.value = false
        passwordHelperStatusType.value = HelperStatusType.NONE
        passwordHelperText.value = ""
        isPasswordStatusNotNone.value = false
        passwordState.value = ""
        emailState.value = ""
    }
}