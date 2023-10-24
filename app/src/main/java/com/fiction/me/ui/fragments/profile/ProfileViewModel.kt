package com.fiction.me.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetAuthTypeUseCase
import com.fiction.domain.interactors.GetAvailableTariffsUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.model.enums.AuthType
import com.fiction.domain.model.profile.ProfileInfo
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getAvailableTariffsUseCase: GetAvailableTariffsUseCase,
    private val getAuthTypeUseCase: GetAuthTypeUseCase
) : BaseViewModel() {

    private val _authType = MutableStateFlow<AuthType?>(null)
    val authType = _authType.asStateFlow()
    init {
        viewModelScope.launch {
            val authType = getAuthTypeUseCase()
            Log.i("ProfileInfo", ": authType = $authType")
            _authType.emit(authType)
        }
    }

    private val _profileInfo = MutableStateFlow<ProfileInfo?>(null)
    val profileInfo = _profileInfo.asStateFlow()

    fun getProfileInfo() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    Log.i("ProfileInfo", "getProfileInfo: ${result.result}")
                    _profileInfo.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun cacheTariffs() {
        viewModelScope.launch {
            getAvailableTariffsUseCase()
        }
    }
}