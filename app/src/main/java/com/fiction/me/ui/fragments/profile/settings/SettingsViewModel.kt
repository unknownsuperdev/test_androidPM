package com.fiction.me.ui.fragments.profile.settings

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.facebook.login.LoginManager
import com.fiction.core.ActionResult
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.ClearAllCashedServerInfoUseCase
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetAuthTypeUseCase
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.LogoutUseCase
import com.fiction.domain.interactors.SetAuthTypeUseCase
import com.fiction.domain.interactors.SetIsDataRestoredUseCase
import com.fiction.domain.model.enums.AuthType
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.ui.fragments.loginregistration.registrationproviders.FacebookSignInProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val setIsDataRestoredUseCase: SetIsDataRestoredUseCase,
    private val udid: Uuid,
    private val clearAllCashedServerInfoUseCase: ClearAllCashedServerInfoUseCase,
    private val getAuthTypeUseCase: GetAuthTypeUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val setAuthTypeUseCase: SetAuthTypeUseCase,
    private val getExploreDataUseCase: ExploreDataUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase,
    private val facebookSignInProvider: FacebookSignInProvider
) : BaseViewModel() {

    private val _authType = MutableStateFlow<AuthType?>(null)
    val authType = _authType.asStateFlow()
    init {
        viewModelScope.launch {
            val authType = getAuthTypeUseCase()
            _authType.emit(authType)
        }
    }

    private val _endedTime = MutableSharedFlow<String>()
    val endedTime = _endedTime.asSharedFlow()

    private val _logout =  MutableStateFlow<Unit?>(null)
    val logout = _logout.asStateFlow()

    val progressState = mutableStateOf(false)
    val isSignOutDialogShow = mutableStateOf(false)

    fun setDataRestored() {
        viewModelScope.launch {
            setIsDataRestoredUseCase(true)
        }
    }
    fun setTimer() {
        viewModelScope.launch {
            delay(3000)
            _endedTime.emit(udid.getUuid())
        }
    }

    fun getUdid() = udid.getUuid() /*{
        viewModelScope.launch {
            delay(3000)
            _endedTime.emit(udid.getUuid())
        }
    }*/

    fun signOut() {
        progressState.value = true
        viewModelScope.launch {
            when (val result = logoutUseCase()) {
                is ActionResult.Success -> {
                    clearAllCashedServerInfoUseCase()
                    val asyncCalls = listOf(
                        async { getCurrentReadingBooksUseCase(isMakeCallAnyway = true) },
                        async { getExploreDataUseCase() },
                        async { getProfileInfoUseCase(isMakeCallAnyWay = true)}
                    )
                    asyncCalls.awaitAll()
                    Log.i("ProfileInfo", "signOut:Success ${result.result}")
                    if(authType.value == AuthType.FACEBOOK){
                        facebookSignInProvider.signOut()
                    }
                    setAuthTypeUseCase(AuthType.GUEST)
                    progressState.value = false
                    _logout.emit(Unit)
                }
                is ActionResult.Error -> {
                    Log.i("ProfileInfo", "signOut:Error ${result.errors.message}")
                    result.errors.message
                    progressState.value = false
                }
            }
        }
    }
}