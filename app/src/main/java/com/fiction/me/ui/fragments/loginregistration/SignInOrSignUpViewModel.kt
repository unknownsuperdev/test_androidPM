package com.fiction.me.ui.fragments.loginregistration

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.ClearAllCashedServerInfoUseCase
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.domain.interactors.LoginWithAppleUseCase
import com.fiction.domain.interactors.LoginWithFBUseCase
import com.fiction.domain.interactors.LoginWithGoogleUseCase
import com.fiction.domain.interactors.SetAuthTypeUseCase
import com.fiction.domain.model.enums.AuthType
import com.fiction.domain.model.registration.Token
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignInOrSignUpViewModel(
    private val loginWithFBUseCase: LoginWithFBUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val clearAllCashedServerInfoUseCase: ClearAllCashedServerInfoUseCase,
    private val setAuthTypeUseCase: SetAuthTypeUseCase,
    private val uuid: Uuid,
    private val getExploreDataUseCase: ExploreDataUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase
) : BaseViewModel() {

    private val _loginToken: MutableSharedFlow<Token?> = MutableSharedFlow()
    val loginToken = _loginToken.asSharedFlow()
    val progressState = mutableStateOf(false)
    fun getGoogleRegisterToken(identifier: String) {
        viewModelScope.launch {
            when (val result = loginWithGoogleUseCase(uuid.getUuid(), identifier)) {
                is ActionResult.Success -> {
                    setAuthTypeUseCase(AuthType.GOOGLE)
                    clearAllServerInfo(result.result)
                }

                is ActionResult.Error -> {
                    progressState.value = false
                    Log.i("SignIn", "getAppleRegisterToken: ${result.errors.message}")
                }
            }
        }
    }

    fun getFbRegisterToken(identifier: String) {
        viewModelScope.launch {
            when (val result = loginWithFBUseCase(uuid.getUuid(), identifier)) {
                is ActionResult.Success -> {
                    setAuthTypeUseCase(AuthType.FACEBOOK)
                    clearAllServerInfo(result.result)
                }

                is ActionResult.Error -> {
                    progressState.value = false
                    Log.i("SignIn", "getAppleRegisterToken: ${result.errors.message}")
                }
            }
        }
    }

    fun getAppleRegisterToken(identifier: String) {

        viewModelScope.launch {
            when (val result = loginWithAppleUseCase(uuid.getUuid(), identifier)) {
                is ActionResult.Success -> {
                    setAuthTypeUseCase(AuthType.APPLE)
                    clearAllServerInfo(result.result)
                }

                is ActionResult.Error -> {
                    progressState.value = false
                    Log.i("SignIn", "getAppleRegisterToken: ${result.errors.message}")
                }
            }
        }
    }

    private fun clearAllServerInfo(token: Token){
        viewModelScope.launch {
            clearAllCashedServerInfoUseCase()
            val asyncCalls = listOf(
                async { getCurrentReadingBooksUseCase(isMakeCallAnyway = true) },
                async { getExploreDataUseCase() },
                async { getProfileInfoUseCase(isMakeCallAnyWay = true) }
            )
            asyncCalls.awaitAll()
            progressState.value = false
            _loginToken.emit(token)
        }
    }
}