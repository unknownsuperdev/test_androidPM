package com.name.jat.ui.fragments.profile

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.GetProfileInfoUseCase
import com.name.domain.interactors.GetUserTokenFromDatastoreUseCase
import com.name.domain.model.profile.ProfileInfo
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserTokenFromDatastoreUseCase: GetUserTokenFromDatastoreUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase
) : BaseViewModel() {

    private val _userToken = MutableStateFlow<String?>(null)
    val userToken = _userToken.asStateFlow()

    private val _profileInfo = MutableStateFlow<ProfileInfo?>(null)
    val profileInfo = _profileInfo.asStateFlow()

    fun getUserToken() {
        viewModelScope.launch {
            getUserTokenFromDatastoreUseCase().collect { token ->
                _userToken.emit(token)
            }
        }
    }

    fun getProfileInfo() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    _profileInfo.emit(result.result)
                }
                is ActionResult.Error -> {
                    // Error handling
                }
            }
        }
    }
}